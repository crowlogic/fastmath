/* This file is public domain. Author: Fredrik Johansson. */


#include <string.h>
#include <acb.h>
#include <flint/fmpz.h>
#include <arb_calc.h>
#include <acb_hypgeom.h>
#include <acb_modular.h>
#include <acb_elliptic.h>
#include <flint/profiler.h>
#include <acb_dirichlet.h>


#include <string.h>
#include "acb.h"
#include "acb_hypgeom.h"
#include "acb_modular.h"
#include "acb_elliptic.h"
#include "flint/profiler.h"

/* some useful color operations */
#define CLAMP(y) FLINT_MAX(0.0, FLINT_MIN((y), 1.0))
#define BLEND(x,y) (0.5*(x) + 0.5*(y))
#define DODGE(a,b) ((a) / ((1.0 - (b)) + 1/256.0))

#define ARB2D(x) arf_get_d(&(x->mid), ARF_RND_NEAR )
#define ARB2DP(x) arf_get_d(x.mid, ARF_RND_NEAR )

/* HLS algorithm from python's colorsys module */
static double
vv(double m1, double m2, double hue)
{
    hue = hue - floor(hue);

    if (hue < 1/6.)
        return m1 + (m2-m1)*hue*6.0;
    if (hue < 0.5)
        return m2;
    if (hue < 2/3.)
        return m1 + (m2-m1)*(2/3.-hue)*6.0;
    return m1;
}

static void
hls_to_rgb(double *R, double *G, double *B, double h, double l, double s)
{
    double m1, m2;

    if (s == 0.0)
    {
        *R = *G = *B = l;
        return;
    }

    if (l <= 0.5)
        m2 = l * (1.0+s);
    else
        m2 = l+s-(l*s);

    m1 = 2.0*l - m2;

    *R = vv(m1, m2, h + 1/3.);
    *G = vv(m1, m2, h);
    *B = vv(m1, m2, h - 1/3.);
}

static void
rgb_to_hls(double *H, double *L, double *S, double R, double G, double B)
{
    double h, l, s, hi, lo, d;

    hi = FLINT_MAX(FLINT_MAX(R, G), B);
    lo = FLINT_MIN(FLINT_MIN(R, G), B);

    l = 0.5 * (lo + hi);
    d = hi - lo;

    if (hi == lo)
    {
        s = 0.0;
        h = 0.0;
    }
    else
    {
        if (l <= 0.5)
            s = d / (hi + lo);
        else
            s = d / (2.0 - hi - lo);

        if (d == 0.0)
            d = 1.0;

        if (R == hi)
            h = (G - B) / d;
        else if (G == hi)
            h = (B - R) / d + 2.0;
        else
            h = (R - G) / d + 4.0;

        h = h / 6.0;
        if (h < 0.0)
            h += 1.0;
    }

    *H = h;
    *L = l;
    *S = s;
}

/* color balance algorithm from gimp */
static double balance_channel(double value, double l,
    double shadows, double midtones, double highlights)
{
    double a = 0.25, b = 0.333, scale = 0.7;

    shadows    *= CLAMP((l - b) / (-a) + 0.5) * scale;
    midtones   *= CLAMP((l - b) / ( a) + 0.5) *
                  CLAMP((l + b - 1.0) / (-a) + 0.5) * scale;
    highlights *= CLAMP((l + b - 1.0) / ( a) + 0.5) * scale;

    value += shadows;
    value += midtones;
    value += highlights;
    return CLAMP(value);
}

static void balance(double * R, double * G, double * B,
    double ra, double rb, double rc,  /* red shadows, midtones, highlights */
    double ga, double gb, double gc,  /* green */
    double ba, double bb, double bc)  /* blue */
{
    double h, l, s;
    double h2, l2, s2;

    rgb_to_hls(&h, &l, &s, *R, *G, *B);

    *R = balance_channel(*R, *R, ra, rb, rc);
    *G = balance_channel(*G, *G, ga, gb, gc);
    *B = balance_channel(*B, *B, ba, bb, bc);

    /* preserve lightness */
    rgb_to_hls(&h2, &l2, &s2, *R, *G, *B);
    hls_to_rgb(R, G, B, h2, l, s2);
}

#define PI 3.1415926535898

const double blue_orange_colors[][4] = {
  {-1.0,  0.0, 0.0, 0.0},
  {-0.95, 0.1, 0.2, 0.5},
  {-0.5,  0.0, 0.5, 1.0},
  {-0.05, 0.4, 0.8, 0.8},
  { 0.0,  1.0, 1.0, 1.0},
  { 0.05, 1.0, 0.9, 0.3},
  { 0.5,  0.9, 0.5, 0.0},
  { 0.95, 0.7, 0.1, 0.0},
  { 1.0,  0.0, 0.0, 0.0},
  { 2.0,  0.0, 0.0, 0.0},
};

void
color_function(double * R, double * G, double * B, const acb_t z, int mode)
{
    double H, L, S;
    slong prec, i;
    arb_t t, u;

    if (!acb_is_finite(z) || acb_rel_accuracy_bits(z) < 4)
    {
        *R = *G = *B = 0.5;
        return;
    }

    if (mode >= 2)
    {
        double R1, G1, B1;
        double R2, G2, B2;

        /* combine both color functions */
        color_function(&R1, &G1, &B1, z, 0);
        color_function(&R2, &G2, &B2, z, 1);

        *R = BLEND(R1, CLAMP(DODGE(R1, R2)));
        *G = BLEND(G1, CLAMP(DODGE(G1, G2)));
        *B = BLEND(B1, CLAMP(DODGE(B1, B2)));

        /* then play with the levels */
        if (mode == 3)
            balance(R, G, B, 0.0, -0.5, 0.2, 0.0, 0.0, -0.1, 0.0, -1.0, -0.2);
        else if (mode == 4)
            balance(R, G, B, 0.0, -0.5, 0.2, 0.0, 0.5, -0.1, 0.0, -0.3, -1.0);
        else if (mode == 5)
            balance(R, G, B, 0.0, -0.5, -1.0, 0.0, -0.1, -0.67, 0.0, -0.55, -0.12);
        else if (mode == 6)
            balance(R, G, B, 0.86, 0.0, 0.13, 0.57, 0.19, -0.52, 0.31, -0.30, -0.94);

        return;
    }

    arb_init(t);
    arb_init(u);

    prec = 32;

    arf_set_round(arb_midref(t), arb_midref(acb_realref(z)), prec, ARF_RND_DOWN);
    arf_set_round(arb_midref(u), arb_midref(acb_imagref(z)), prec, ARF_RND_DOWN);

    arb_atan2(t, u, t, prec);

    H = arf_get_d(arb_midref(t), ARF_RND_DOWN);

    if (mode == 0)
    {
        H = (H + PI) / (2 * PI) + 0.5;
        H = H - floor(H);

        acb_abs(t, z, prec);

        if (arf_cmpabs_2exp_si(arb_midref(t), 200) > 0)
        {
            L = 1.0;
        }
        else if (arf_cmpabs_2exp_si(arb_midref(t), -200) < 0)
        {
            L = 0.0;
        }
        else
        {
            L = arf_get_d(arb_midref(t), ARF_RND_DOWN);
            L = 1.0 - 1.0/(1.0 + pow(L, 0.2));
        }

        S = 0.8;

        hls_to_rgb(R, G, B, H, L, S);
    }
    else
    {
        H = H / PI;
        H = FLINT_MAX(FLINT_MIN(H, 1.0), -1.0);

        for (i = 1; ; i++)
        {
            if (blue_orange_colors[i][0] > H)
            {
                double a, ra, ga, ba, b, rb, gb, bb, s;

                a  = blue_orange_colors[i-1][0];
                ra = blue_orange_colors[i-1][1];
                ga = blue_orange_colors[i-1][2];
                ba = blue_orange_colors[i-1][3];
                b  = blue_orange_colors[i][0];
                rb = blue_orange_colors[i][1];
                gb = blue_orange_colors[i][2];
                bb = blue_orange_colors[i][3];

                s = (H - a) / (b - a);
                *R = ra + (rb - ra) * s;
                *G = ga + (gb - ga) * s;
                *B = ba + (bb - ba) * s;
                break;
            }
        }
    }

    arb_clear(t);
    arb_clear(u);
}

typedef void (*func_ptr)(acb_t, const acb_t, slong);

void
ai(acb_t res, const acb_t z, slong prec)
{
    acb_hypgeom_airy(res, NULL, NULL, NULL, z, prec);
}

void
bi(acb_t res, const acb_t z, slong prec)
{
    acb_hypgeom_airy(NULL, NULL, res, NULL, z, prec);
}

void
besselj(acb_t res, const acb_t z, slong prec)
{
    acb_t nu;
    acb_init(nu);
    acb_hypgeom_bessel_j(res, nu, z, prec);
    acb_clear(nu);
}

void
bessely(acb_t res, const acb_t z, slong prec)
{
    acb_t nu;
    acb_init(nu);
    acb_hypgeom_bessel_y(res, nu, z, prec);
    acb_clear(nu);
}

void
besseli(acb_t res, const acb_t z, slong prec)
{
    acb_t nu;
    acb_init(nu);
    acb_hypgeom_bessel_i(res, nu, z, prec);
    acb_clear(nu);
}

void
besselk(acb_t res, const acb_t z, slong prec)
{
    acb_t nu;
    acb_init(nu);
    acb_hypgeom_bessel_k(res, nu, z, prec);
    acb_clear(nu);
}

/* this function looks better when scaled */
void
modj(acb_t res, const acb_t z, slong prec)
{
    acb_modular_j(res, z, prec);
    acb_div_ui(res, res, 1728, prec);
}

void
modjq(acb_t res, const acb_t z, slong prec)
{
    acb_t t;
    acb_init(t);
    acb_log(res, z, prec);
    acb_const_pi(t, prec);
    acb_div(res, res, t, prec);
    acb_mul_2exp_si(res, res, -1);
    acb_div_onei(res, res);
    acb_modular_j(res, res, prec);
    acb_div_ui(res, res, 1728, prec);
    acb_clear(t);
}

void
modetaq(acb_t res, const acb_t z, slong prec)
{
    acb_t t;
    acb_init(t);
    acb_log(res, z, prec);
    acb_const_pi(t, prec);
    acb_div(res, res, t, prec);
    acb_mul_2exp_si(res, res, -1);
    acb_div_onei(res, res);
    acb_modular_eta(res, res, prec);
    acb_clear(t);
}

void
modlambdaq(acb_t res, const acb_t z, slong prec)
{
    acb_t t;
    acb_init(t);
    acb_log(res, z, prec);
    acb_const_pi(t, prec);
    acb_div(res, res, t, prec);
    acb_mul_2exp_si(res, res, -1);
    acb_div_onei(res, res);
    acb_modular_lambda(res, res, prec);
    acb_clear(t);
}

void
ellipp(acb_t res, const acb_t z, slong prec)
{
    acb_onei(res);
    acb_elliptic_p(res, z, res, prec);
}

void
ellipzeta(acb_t res, const acb_t z, slong prec)
{
    acb_onei(res);
    acb_elliptic_zeta(res, z, res, prec);
}

void
ellipsigma(acb_t res, const acb_t z, slong prec)
{
    acb_onei(res);
    acb_elliptic_sigma(res, z, res, prec);
}

void
fresnels(acb_t res, const acb_t z, slong prec)
{
    acb_hypgeom_fresnel(res, NULL, z, 0, prec);
}

void
fresnelc(acb_t res, const acb_t z, slong prec)
{
    acb_hypgeom_fresnel(NULL, res, z, 0, prec);
}


/**
 * tanh(ln(1+Z(t)^2))
 */
void Y(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);
}

/**
 * Re tanh(ln(1+Z(t)^2))
 */
void Yreal(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);
	arb_set_d( &(res->imag), 0 );
}

/**
 * Re Z(t)
 */
void Zreal(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	arb_set_d( &(res->imag), 0 );
}

/**
 * Im tanh(ln(1+Z(t)^2))
 */
void Yimag(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);
	arb_set_d( &(res->real), 0 );
   
}

/**
 * Im(Z(t))
 */
void Zimag(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	arb_set_d( &(res->real), 0 );
}

/**
 * Re tanh(ln(1+Z(t)^2))
 */
void Yabs(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);
	acb_abs(&(res->real), res, prec );
	arb_set_d( &(res->imag), 0 );
}


double SIGN(double x) {
  if (x > 0.0) return 1.0;
  if (x < 0.0) return -1.0;
  return x;
}

/**
 * sign(Re tanh(ln(1+Z(t)^2)))
 */
void YRealSign(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);
	//arb_
	//arb_set_d( &(res->imag), SIGN( arb_get) );
	//acb_arg(&(res->imag), res, prec);
	arb_set_d( &(res->imag), 0 );
	acb_sgn(res, res, prec);
//	double rv = ARB2DP(&(z->real));
//	double iv = ARB2DP(&(z->imag));
//	int flip = SIGN(rv)*SIGN(rv);
//	acb_mul_si(res, res, flip, prec);

}

/**
 * sign(Im tanh(ln(1+Z(t)^2)))
 */
void YImagSign(acb_t res, const acb_t z, slong prec)
{
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);
	//arb_
	//arb_set_d( &(res->imag), SIGN( arb_get) );
	//acb_arg(&(res->imag), res, prec);
	arb_set_d( &(res->real), 0 );
	acb_sgn(res, res, prec);
//	double rv = ARB2DP(&(z->real));
//	double iv = ARB2DP(&(z->imag));
//	int flip = SIGN(rv)*SIGN(rv);
//	acb_mul_si(res, res, flip, prec);

}
/**
 * Im tanh(ln(1+Z(t)^2))
 */
void YimagSign(acb_t res, const acb_t z, slong prec)
{
	;
	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);
	//arb_
	//arb_set_d( &(res->imag), SIGN( arb_get) );
	acb_arg(&(res->imag), res, prec);
	arb_set_d( &(res->real), 0 );
	acb_sgn(res, res, prec);
	double rv = ARB2DP(&(z->real));
	double iv = ARB2DP(&(z->imag));
	int flip = SIGN(iv)*SIGN(rv);
	acb_mul_si(res, res, flip, prec);

}


/**
 *       diff(tanh(ln(1+Z(t)^2)),t)
 *                   =
 *            (8*(1+Z(t)^2))*Z(t)*Z'(t)
 *  ----------------------------------------------------
 *            (Z(t)^4+2*Z(t)^2+2)^2
 */
void YFirstDerivative(acb_t res, const acb_t t, slong prec)
{
	acb_t Z[2];
	acb_init( Z[0]);
	acb_init( Z[1]);
	acb_dirichlet_hardy_z((acb_ptr)&Z, t, NULL, NULL, 2, prec);
	printf("t=");
	acb_printd(t,15);
	printf("\n");

	printf("Z[0]=");
	acb_printd(Z[0],15);
	printf("\n");

	printf("Z[1]=");
	acb_printd(Z[1],15);
	printf("\n");

	//exit(1);

	acb_t z4, z2;
	acb_init(z2);
	acb_init(z4);

	acb_pow_ui(z2, t, 2, prec );
	acb_pow_ui(z4, t, 4, prec );

	acb_pow_ui(res, res, 2, prec );
	acb_add_ui(res, res, 1, prec);
	acb_log(res, res, prec);
	acb_tanh(res, res, prec);

	acb_clear(z2);
	acb_clear(z4);
	acb_clear(Z[0]);
	acb_clear(Z[1]);


}

void circle( acb_t res, acb_t z, const arb_t radius, const arb_t angle, slong prec )
{
	acb_t displacement;
	acb_init(displacement);
	acb_set(res, z);
	arb_cos( &(displacement->real), angle, prec );
	arb_sin( &(displacement->imag), angle, prec );
	acb_mul_arb(displacement, displacement, radius, prec );
	acb_add(res, res, displacement, prec );
	acb_clear(displacement);
}

void Z(acb_t res, const acb_t z, slong prec)
{

	acb_dirichlet_hardy_z(res, z, NULL, NULL, 1, prec);
}


const int verbose = 0;

void Ycircle(acb_t res, acb_t center, arb_t r, const arb_t angle, int prec, acb_t y)
{
	circle(res, center, r, angle, prec);
	Y(y, res, prec);
}
void rho( acb_t res, const arb_t r, const arb_t a, const slong prec )
{
	// return r*(-sin(a)+I*cos(a))

	arb_sin_cos(&(res->real), &(res->imag), a, prec);
	arb_mul_si(&(res->real), &(res->real), -1, prec);
	acb_mul_arb(res, res, r, prec );

}
const int prec = 512;

void YcircleFirstDerivative(acb_t res, acb_t center, arb_t r, const arb_t angle, int prec, acb_t y)
{
	printf( "Entering YcircleFirstDerivative\n");
	 printf( " center=" );
	 acb_printd( center, 15 );
	 printf("\n");
	 printf( " radius=" );
	 arb_printd( r, 15 );
	 printf("\n");
	 printf( " angle=" );
	 arb_printd( angle, 15 );
	 printf("\n");
	 printf(" prec=%d\n", prec );

	circle(res, center, r, angle, prec);
	acb_t dy;
	acb_init(dy);
	YFirstDerivative(dy, res, prec);
	// t = res
	//
	// (8*(1+Y(t)^2))*Y(t)*(D(Y))(t)*rho(r,a)
    //	      -------------------------------------------------
	///                   (Y(t)^4+2*Y(t)^2+2)^2

	Y(y, res, prec);
	acb_t y2;
	acb_init(y2);
	acb_t y4;
	acb_init(y4);
	acb_pow_ui(y2, y, 2, prec );
	acb_pow_ui(y4, y, 4, prec );

	acb_t denom; //  (Y(t)^4+2*Y(t)^2+2)^2
	acb_init(denom);

	acb_set( denom, y2 );
	acb_mul_ui(denom, denom, 2, prec );
	acb_add_ui( denom, denom, 2, prec );
	acb_add( denom, denom, y4, prec );
	acb_pow_ui( denom, denom, 2, prec );


	acb_t numer; // (8*(1+Y(t)^2))*Y(t)*(D(Y))(t)*rho*(r,a)
	acb_init(numer);
	acb_set( numer, y2 );
	acb_add_ui( numer, numer, 1, prec );
	acb_mul_ui( numer, numer, 8, prec );
	acb_mul( numer, numer, y, prec );
	acb_mul( numer, numer, dy, prec );

	acb_t rhoval;
	acb_init(rhoval);
	rho( rhoval, r, angle, prec );
	acb_mul( numer, numer, rhoval, prec );

	acb_div(res, numer, denom, prec);

	printf( " rho = ");
	acb_printd( rhoval, 15);
	printf("\n");
	printf( "Ycircle' numer = ");
	acb_printd( numer, 15);
	printf("\n");
	printf( "Ycircle' denom = ");
	acb_printd( denom, 15);
	printf("\n");
	printf( "Ycircle'  = ");
	acb_printd( res, 15);
	printf("\n");
	printf( "angle=");
	arb_printd( angle, 15 );
	printf("\n");

	acb_clear(y2);
	acb_clear(y4);
	acb_clear(numer);
	acb_clear(denom);


}


typedef struct
{
    acb_t center;
    arb_t radius;
}
z_param_struct;

/**
 *  arb_calc_func_t

 Typedef for a pointer to a function with signature:

 int func(arb_ptr out, const arb_t inp, void * param, slong order, slong prec)

 implementing a univariate real function f(x) .
 When called, func should write to out the first order coefficients in the Taylor series expansion of f(x) at the point inp, evaluated at a precision of prec bits.
 The param argument may be used to pass through additional parameters to the function. The return value is reserved for future use as an error code.
 It can be assumed that out and inp are not aliased and that order is positive.
 */
int Yreal_function(arb_ptr out, const arb_t angle, void * params, slong order, slong prec)
{
    z_param_struct * par = (z_param_struct*)params;

    acb_t nextz, nextx;
    acb_init(nextz);
    acb_init(nextx);
   // printf(" params=%p\n", params );

    switch(order)
    {
    case 1:
    Ycircle(nextx, par->center, par->radius, angle, prec, nextz);
    arb_set( out + 0, &(nextz->real) );

    break;
    case 2:
        Ycircle(nextx, par->center, par->radius, angle, prec, nextz);
        printf("Yreal_function=" );
        acb_printd( nextx, 15 );
        printf("\n");
        arb_set( out + 0, &(nextz->real) );
    	YcircleFirstDerivative(nextx, par->center, par->radius, angle, prec, nextz);
        printf("Yreal_function'=" );
        acb_printd( nextx, 15 );
        printf("\n");
        arb_set( out + 1, &(nextz->real) );
    	break;
    default:
    	printf(" TODO: handle higher order derivatives, order=%ld\n", order );
    	exit(1);
    }

    acb_clear(nextz);
    acb_clear(nextx);

    return 0;
}

int main(int argc, char *argv[])
{
    slong x, y, xnum, ynum, prec, i;
    double R, G, B, dxa, dxb, dya, dyb;
    FILE * fp;
    arf_t xa, xb, ya, yb;
    acb_t z, w;
    func_ptr func;
    int color_mode;
    char *outfile = "arbplot.ppm";

    if (argc < 2)
    {
        printf("complex_plot [-out file.ppm] [-range xa xb ya yb] [-size xn yn] [-color n] <func>\n\n");

        printf("Plots one of the predefined functions on [xa,xb] + [ya,yb]i\n");
        printf("using domain coloring, at a resolution of xn by yn pixels.\n\n");

        printf("Defaults parameters are [-10,10] + [-10,10]i and xn = yn = 512.\n\n");

        printf("A color function can be selected with -color. The choices are:\n");
        printf("0   phase=hue, magnitude=brightness\n");
        printf("1   phase only, white-gold-black-blue-white counterclockwise\n");
        printf("2   0+1 (dodge filter)\n");
        printf("3   0+1, shiny\n");
        printf("4   0+1, warm\n");
        printf("5   0+1, cold\n");
        printf("6   0+1, tomato\n\n");

        printf("The output is written to arbplot.ppm. If you have ImageMagick,\n");
        printf("run [convert arbplot.ppm arbplot.png] to get a PNG.\n\n");

        printf("Function codes <func> are:\n");
        printf("  sin        - Sine\n");
        printf("  gamma      - Gamma function\n");
        printf("  digamma    - Digamma function\n");
        printf("  lgamma     - Logarithmic gamma function\n");
        printf("  zeta       - Riemann zeta function\n");
        printf("  erf        - Error function\n");
        printf("  ai         - Airy function Ai\n");
        printf("  bi         - Airy function Bi\n");
        printf("  besselj    - Bessel function J_0\n");
        printf("  bessely    - Bessel function Y_0\n");
        printf("  besseli    - Bessel function I_0\n");
        printf("  besselk    - Bessel function K_0\n");
        printf("  modj       - Modular j-function\n");
        printf("  modjq      - Modular j-function (as function of q)\n");
        printf("  modeta     - Dedekind eta function\n");
        printf("  modetaq    - Dedekind eta function (as function of q)\n");
        printf("  modlambda  - Modular lambda function\n");
        printf("  modlambdaq - Modular lambda function (as function of q)\n");
        printf("  ellipp     - Weierstrass elliptic function (on square lattice)\n");
        printf("  ellipzeta  - Weierstrass elliptic function (on square lattice)\n");
        printf("  ellipsigma - Weierstrass elliptic function (on square lattice)\n");
        printf("  barnesg    - Barnes G-function\n");
        printf("  agm        - Arithmetic geometric mean\n");
        printf("  fresnels   - Fresnel integral S\n");
        printf("  fresnelc   - Fresnel integral C\n\n");
        printf("  z          - Z(t)\n");
        printf("  zreal      - Re(Z(t))\n");
        printf("  zimag      - Im(Z(t))\n");
        printf("  y          - tanh(ln(1+Z(t)^2))\n");
        printf("  yreal      - Re(tanh(ln(1+Z(t)^2)))\n");
        printf("  yimag      - Im(tanh(ln(1+Z(t)^2)))\n");
        printf("  yabs       - |Im(tanh(ln(1+Z(t)^2)))|\n");
        printf("  yrealsign  - sign(Re(tanh(ln(1+Z(t)^2))))\n");

        return 1;
    }

    xnum = 512;
    ynum = 512;
    dxa = dya = -10;
    dxb = dyb = 10;
    func = acb_gamma;
    color_mode = 0;

    for (i = 1; i < argc; i++)
    {
        if (!strcmp(argv[i], "-size"))
        {
            xnum = atol(argv[i+1]);
            ynum = atol(argv[i+2]);
            i += 2;
        }
        else if (!strcmp(argv[i], "-range"))
        {
            dxa = atof(argv[i+1]);
            dxb = atof(argv[i+2]);
            dya = atof(argv[i+3]);
            dyb = atof(argv[i+4]);
            i += 4;
        }
        else if (!strcmp(argv[i], "-color"))
        {
            color_mode = atoi(argv[i+1]);
            i++;
        }
        else if (!strcmp(argv[i], "-out"))
        {
            outfile = argv[i+1];
            i++;
        }
        else if (!strcmp(argv[i], "sin"))
            func = acb_sin;
        else if (!strcmp(argv[i], "gamma"))
            func = acb_gamma;
        else if (!strcmp(argv[i], "digamma"))
            func = acb_digamma;
        else if (!strcmp(argv[i], "lgamma"))
            func = acb_lgamma;
        else if (!strcmp(argv[i], "zeta"))
            func = acb_zeta;
        else if (!strcmp(argv[i], "erf"))
            func = acb_hypgeom_erf;
        else if (!strcmp(argv[i], "ai"))
            func = ai;
        else if (!strcmp(argv[i], "bi"))
            func = bi;
        else if (!strcmp(argv[i], "besselj"))
            func = besselj;
        else if (!strcmp(argv[i], "bessely"))
            func = bessely;
        else if (!strcmp(argv[i], "besseli"))
            func = besseli;
        else if (!strcmp(argv[i], "besselk"))
            func = besselk;
        else if (!strcmp(argv[i], "modj"))
            func = modj;
        else if (!strcmp(argv[i], "modjq"))
            func = modjq;
        else if (!strcmp(argv[i], "modeta"))
            func = acb_modular_eta;
        else if (!strcmp(argv[i], "modetaq"))
            func = modetaq;
        else if (!strcmp(argv[i], "modlambda"))
            func = acb_modular_lambda;
        else if (!strcmp(argv[i], "modlambdaq"))
            func = modlambdaq;
        else if (!strcmp(argv[i], "ellipp"))
            func = ellipp;
        else if (!strcmp(argv[i], "ellipzeta"))
            func = ellipzeta;
        else if (!strcmp(argv[i], "ellipsigma"))
            func = ellipsigma;
        else if (!strcmp(argv[i], "barnesg"))
            func = acb_barnes_g;
        else if (!strcmp(argv[i], "agm"))
            func = acb_agm1;
        else if (!strcmp(argv[i], "fresnels"))
            func = fresnels;
        else if (!strcmp(argv[i], "fresnelc"))
            func = fresnelc;
        else if (!strcmp(argv[i], "z"))
            func = Z;
        else if (!strcmp(argv[i], "zreal"))
            func = Zreal;
        else if (!strcmp(argv[i], "zimag"))
            func = Zimag;
        else if (!strcmp(argv[i], "y"))
            func = Y;
        else if (!strcmp(argv[i], "yreal"))
            func = Yreal;
        else if (!strcmp(argv[i], "yimag"))
            func = Yimag;
        else if (!strcmp(argv[i], "yabs"))
            func = Yabs;
        else if (!strcmp(argv[i], "yrealsign"))
            func = YRealSign;
        else if (!strcmp(argv[i], "yimagsign"))
            func = YImagSign;
        else
        {
            printf("unknown option: %s\n", argv[i]);
            return 1;
        }
    }

    acb_init(z);
    acb_init(w);

    arf_init(xa);
    arf_init(xb);
    arf_init(ya);
    arf_init(yb);

    arf_set_d(xa, dxa);
    arf_set_d(xb, dxb);
    arf_set_d(ya, dya);
    arf_set_d(yb, dyb);

    fp = fopen(outfile, "w");
    fprintf(fp, "P6\n%ld %ld 255\n", xnum, ynum);

    TIMEIT_ONCE_START

    for (y = ynum - 1; y >= 0; y--)
    {
        if (y % (ynum / 16) == 0)
            printf("row %ld\n", y);

        for (x = 0; x < xnum; x++)
        {
            for (prec = 30; prec < 500; prec *= 2)
            {
                arf_sub(arb_midref(acb_imagref(z)), yb, ya, prec, ARF_RND_DOWN);
                arf_mul_ui(arb_midref(acb_imagref(z)),
                    arb_midref(acb_imagref(z)), y, prec, ARF_RND_DOWN);
                arf_div_ui(arb_midref(acb_imagref(z)),
                    arb_midref(acb_imagref(z)), ynum - 1, prec, ARF_RND_DOWN);
                arf_add(arb_midref(acb_imagref(z)),
                    arb_midref(acb_imagref(z)), ya, prec, ARF_RND_DOWN);

                arf_sub(arb_midref(acb_realref(z)), xb, xa, prec, ARF_RND_DOWN);
                arf_mul_ui(arb_midref(acb_realref(z)),
                    arb_midref(acb_realref(z)), x, prec, ARF_RND_DOWN);
                arf_div_ui(arb_midref(acb_realref(z)),
                    arb_midref(acb_realref(z)), xnum - 1, prec, ARF_RND_DOWN);
                arf_add(arb_midref(acb_realref(z)),
                    arb_midref(acb_realref(z)), xa, prec, ARF_RND_DOWN);

                func(w, z, prec);

                if (acb_rel_accuracy_bits(w) > 4)
                    break;
            }

            color_function(&R, &G, &B, w, color_mode);

            fputc(FLINT_MIN(255, floor(R * 255)), fp);
            fputc(FLINT_MIN(255, floor(G * 255)), fp);
            fputc(FLINT_MIN(255, floor(B * 255)), fp);
        }
    }

    TIMEIT_ONCE_STOP

    fclose(fp);

    arf_clear(xa);
    arf_clear(xb);
    arf_clear(ya);
    arf_clear(yb);

    acb_clear(z);
    acb_clear(w);

    flint_cleanup();
    return 0;
}

