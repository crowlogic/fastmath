/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class acb_dirichlet_platt_i_precomp_struct {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected acb_dirichlet_platt_i_precomp_struct(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(acb_dirichlet_platt_i_precomp_struct obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        arblibJNI.delete_acb_dirichlet_platt_i_precomp_struct(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setC1(arb_struct value) {
    arblibJNI.acb_dirichlet_platt_i_precomp_struct_c1_set(swigCPtr, this, arb_struct.getCPtr(value), value);
  }

  public arb_struct getC1() {
    long cPtr = arblibJNI.acb_dirichlet_platt_i_precomp_struct_c1_get(swigCPtr, this);
    return (cPtr == 0) ? null : new arb_struct(cPtr, false);
  }

  public void setC2(arb_struct value) {
    arblibJNI.acb_dirichlet_platt_i_precomp_struct_c2_set(swigCPtr, this, arb_struct.getCPtr(value), value);
  }

  public arb_struct getC2() {
    long cPtr = arblibJNI.acb_dirichlet_platt_i_precomp_struct_c2_get(swigCPtr, this);
    return (cPtr == 0) ? null : new arb_struct(cPtr, false);
  }

  public acb_dirichlet_platt_i_precomp_struct() {
    this(arblibJNI.new_acb_dirichlet_platt_i_precomp_struct(), true);
  }

}
