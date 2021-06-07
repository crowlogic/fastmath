%module arblib

%include typemaps.i

%apply double *OUTPUT { double *R };
%apply double *OUTPUT { double *G };
%apply double *OUTPUT { double *B };


#define _GLIBCXX_BEGIN_NAMESPACE_VERSION
#define _GLIBCXX_END_NAMESPACE_VERSION
#define _GLIBCXX_BEGIN_NAMESPACE_CXX11
#define _GLIBCXX_END_NAMESPACE_CXX11

//#define __fpos_t = long int
//#define fpos_t = long int

#define __signed__

#define slong signed long
#define ulong unsigned long

%typemap(in) slong = long;

#define fmpr_rnd_t int
#define arf_rnd_t int

//#define FLINT_TLS_PREFIX 
//#define TLS_PREFIX
#define FLINT_DLL
//#define ARB_DLL
#define ARB_INLINES_C
#define ACB_INLINES_C
#define ACB_DIRICHLET_INLINES_C
#define DIRICHLET_INLINES_C
#define ACB_POLY_INLINES_C

%{
#include <mpfr.h>
#include <flint/flint.h>
#include <arf.h>
#include <arb.h>
#include <acb.h>
#include <acb_poly.h>
#include <acb_dirichlet.h>
#include <dirichlet.h>
#include "complex_plot.h"
%}

void
color_function(double * R, double * G, double * B, const acb_t z, int mode);

typedef struct
{
    arb_struct real;
    arb_struct imag;
}
acb_struct;

typedef acb_struct acb_t[1];
typedef acb_struct * acb_ptr;
typedef const acb_struct * acb_srcptr;

typedef struct
{
    ulong q;                /* modulus */
    ulong q_even;           /* even part of modulus */
    nmod_t mod;             /* modulus with precomputed inverse */
    ulong rad_q;            /* radical = product of odd primes */
    ulong phi_q;            /* phi(q) = group size */
    slong neven;            /* number of even components (in 0,1,2)*/
    slong num;              /* number of prime components (even + odd) */
    ulong expo;             /* exponent = largest order in G */
    dirichlet_prime_group_struct * P;
    ulong * generators;     /* generators lifted mod q */
    ulong * PHI;            /* PHI(k) = expo / phi(k)        */
}
dirichlet_group_struct;

typedef dirichlet_group_struct dirichlet_group_t[1];

typedef struct
{
    ulong n;           /* number */
    ulong * log;       /* s.t. prod generators[k]^log[k] = number */
}
dirichlet_char_struct;

typedef dirichlet_char_struct dirichlet_char_t[1];

typedef struct
{
    ulong p;    /* underlying prime */
    int e;      /* exponent */
    nmod_t pe;  /* modulus */
    nmod_t phi;  /* phi(p^e) */
    ulong g;    /* conrey generator */
    dlog_precomp_struct * dlog;  /* precomputed data for discrete log mod p^e */
}
dirichlet_prime_group_struct;

typedef dirichlet_group_struct dirichlet_group_t[1];

typedef struct
{
    arf_struct mid;
    mag_struct rad;
}
arb_struct;

typedef arb_struct arb_t[1];
typedef arb_struct * arb_ptr;
typedef const arb_struct * arb_srcptr;

void
arf_init(arf_t x)
{
    fmpz_init(ARF_EXPREF(x));
    ARF_XSIZE(x) = 0;
}

void acb_init(acb_t x)
{
    arb_init(acb_realref(x));
    arb_init(acb_imagref(x));
}

typedef struct
{
    mp_limb_t d[ARF_NOPTR_LIMBS];
}
mantissa_noptr_struct;

typedef struct
{
    mp_size_t alloc;
    mp_ptr d;
}
mantissa_ptr_struct;

typedef union
{
    mantissa_noptr_struct noptr;
    mantissa_ptr_struct ptr;
}
mantissa_struct;

typedef struct
{
    fmpz exp;
    mp_size_t size;
    mantissa_struct d;
}
arf_struct;

void arb_init(arb_t x)
{
    arf_init(arb_midref(x));
    mag_init(arb_radref(x));
}

typedef arf_struct arf_t[1];
typedef arf_struct * arf_ptr;
typedef const arf_struct * arf_srcptr;

extern void acb_set_d(acb_t z, double c);

extern void acb_dirichlet_hardy_z(acb_ptr res, 
                                  const acb_t t, 
								  const dirichlet_group_t G, 
								  const dirichlet_char_t chi,
								  slong len, 
								  slong prec);
	
extern void acb_print(const acb_t x);
	 			  
extern char *arb_get_str(const arb_t x, slong n, ulong flags);

extern double arf_get_d(const arf_t x, arf_rnd_t rnd);

extern void arf_set_d(arf_t x, double v);

extern int arf_sub(arf_ptr z, arf_srcptr x, arf_srcptr y, slong prec, arf_rnd_t rnd);

extern void arb_mul_ui(arb_t z, const arb_t x, ulong y, slong prec);

extern int arf_mul_ui(arf_ptr z, arf_srcptr x, ulong y, slong prec, arf_rnd_t rnd)
{
    arf_t t;
    arf_init_set_ui(t, y); /* no need to free */
    return arf_mul(z, x, t, prec, rnd);
}

extern int arf_div_ui(arf_ptr z, arf_srcptr x, ulong y, slong prec, arf_rnd_t rnd)
{
    arf_t t;
    arf_init_set_ui(t, y); /* no need to free */
    return arf_div(z, x, t, prec, rnd);
}

extern int arf_add(arf_ptr z, arf_srcptr x, arf_srcptr y, slong prec, arf_rnd_t rnd);

extern slong acb_rel_accuracy_bits(const acb_t x)
{
    return -acb_rel_error_bits(x);
}

extern void acb_dirichlet_hardy_z(acb_ptr res, const acb_t t, const dirichlet_group_t G, const dirichlet_char_t chi, slong len, slong prec);


extern void acb_pow_ui(acb_t y, const acb_t b, ulong e, slong prec);

extern void acb_add_ui(acb_t z, const acb_t x, ulong c, slong prec)
{
    arb_add_ui(acb_realref(z), acb_realref(x), c, prec);
    arb_set_round(acb_imagref(z), acb_imagref(x), prec);
}

extern void acb_log(acb_t r, const acb_t z, slong prec);

extern void acb_tanh(acb_t y, const acb_t x, slong prec)
{
    acb_mul_onei(y, x);
    acb_tan(y, y, prec);
    acb_div_onei(y, y);
}


extern void color_function(double * R, double * G, double * B, const acb_t z, int mode);

extern void arb_set_d(arb_t x, double y);
        
extern void acb_dirichlet_hurwitz(acb_t res, const acb_t s, const acb_t a, slong prec);

