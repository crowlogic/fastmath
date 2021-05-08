%module arblib

#define _GLIBCXX_BEGIN_NAMESPACE_VERSION
#define _GLIBCXX_END_NAMESPACE_VERSION
#define _GLIBCXX_BEGIN_NAMESPACE_CXX11
#define _GLIBCXX_END_NAMESPACE_CXX11

//#define __fpos_t = long int
//#define fpos_t = long int

#define __signed__

%typemap(in) slong = long;

//#define TLS_PREFIX
#define FLINT_DLL
//#define ARB_DLL
#define ARB_INLINES_C
#define ACB_INLINES_C
#define ACB_DIRICHLET_INLINES_C
#define DIRICHLET_INLINES_C
#define ACB_POLY_INLINES_C

%include "/usr/include/mpfr.h"
%include "/usr/include/x86_64-linux-gnu/gmp.h"
%include "/usr/include/flint/flint.h"
%include "/usr/include/arf.h"
%include "/usr/include/arb.h"
%include "/usr/include/acb.h"
%include "/usr/include/acb_poly.h"
%include "/usr/include/acb_dirichlet.h"
%include "/usr/include/dirichlet.h"
