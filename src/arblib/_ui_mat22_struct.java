/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class _ui_mat22_struct {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected _ui_mat22_struct(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(_ui_mat22_struct obj) {
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
        arblibJNI.delete__ui_mat22_struct(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void set_11(long value) {
    arblibJNI._ui_mat22_struct__11_set(swigCPtr, this, value);
  }

  public long get_11() {
    return arblibJNI._ui_mat22_struct__11_get(swigCPtr, this);
  }

  public void set_12(long value) {
    arblibJNI._ui_mat22_struct__12_set(swigCPtr, this, value);
  }

  public long get_12() {
    return arblibJNI._ui_mat22_struct__12_get(swigCPtr, this);
  }

  public void set_21(long value) {
    arblibJNI._ui_mat22_struct__21_set(swigCPtr, this, value);
  }

  public long get_21() {
    return arblibJNI._ui_mat22_struct__21_get(swigCPtr, this);
  }

  public void set_22(long value) {
    arblibJNI._ui_mat22_struct__22_set(swigCPtr, this, value);
  }

  public long get_22() {
    return arblibJNI._ui_mat22_struct__22_get(swigCPtr, this);
  }

  public void setDet(int value) {
    arblibJNI._ui_mat22_struct_det_set(swigCPtr, this, value);
  }

  public int getDet() {
    return arblibJNI._ui_mat22_struct_det_get(swigCPtr, this);
  }

  public _ui_mat22_struct() {
    this(arblibJNI.new__ui_mat22_struct(), true);
  }

}
