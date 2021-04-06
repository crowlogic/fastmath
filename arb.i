%module arblib

#define ARB_DLL
#define ARB_INLINES_C
#define ACB_INLINES_C
#define ACB_DIRICHLET_INLINES_C
#define DIRICHLET_INLINES_C
#define ACB_POLY_INLINES_C

%include "/usr/include/arb.h"
%include "/usr/include/acb.h"
%include "/usr/include/acb_poly.h"
%include "/usr/include/acb_dirichlet.h"
%include "/usr/include/dirichlet.h"

