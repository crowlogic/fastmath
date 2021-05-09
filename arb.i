%module arblib

%{
#include <mpfr.h>
#include <flint/flint.h>
#include <arf.h>
#include <arb.h>
#include <acb.h>
#include <acb_poly.h>
#include <acb_dirichlet.h>
#include <dirichlet.h>
%}

#define _GLIBCXX_BEGIN_NAMESPACE_VERSION
#define _GLIBCXX_END_NAMESPACE_VERSION
#define _GLIBCXX_BEGIN_NAMESPACE_CXX11
#define _GLIBCXX_END_NAMESPACE_CXX11

//#define __fpos_t = long int
//#define fpos_t = long int

#define __signed__

%typemap(in) slong = long;

//#define FLINT_TLS_PREFIX 
//#define TLS_PREFIX
#define FLINT_DLL
//#define ARB_DLL
#define ARB_INLINES_C
#define ACB_INLINES_C
#define ACB_DIRICHLET_INLINES_C
#define DIRICHLET_INLINES_C
#define ACB_POLY_INLINES_C

typedef struct
{
    arb_struct real;
    arb_struct imag;
}
acb_struct;

void acb_set_d(acb_t z, double c);

extern void acb_dirichlet_hardy_z(acb_ptr res, 
                                  const acb_t t, 
				  const dirichlet_group_t G, 
				  const dirichlet_char_t chi,
				  slong len, 
				  slong prec);
				  
