/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class fmpz_multi_crt_struct {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected fmpz_multi_crt_struct(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(fmpz_multi_crt_struct obj) {
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
        arblibJNI.delete_fmpz_multi_crt_struct(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setProg(_fmpz_multi_crt_prog_instr value) {
    arblibJNI.fmpz_multi_crt_struct_prog_set(swigCPtr, this, _fmpz_multi_crt_prog_instr.getCPtr(value), value);
  }

  public _fmpz_multi_crt_prog_instr getProg() {
    long cPtr = arblibJNI.fmpz_multi_crt_struct_prog_get(swigCPtr, this);
    return (cPtr == 0) ? null : new _fmpz_multi_crt_prog_instr(cPtr, false);
  }

  public void setLength(int value) {
    arblibJNI.fmpz_multi_crt_struct_length_set(swigCPtr, this, value);
  }

  public int getLength() {
    return arblibJNI.fmpz_multi_crt_struct_length_get(swigCPtr, this);
  }

  public void setAlloc(int value) {
    arblibJNI.fmpz_multi_crt_struct_alloc_set(swigCPtr, this, value);
  }

  public int getAlloc() {
    return arblibJNI.fmpz_multi_crt_struct_alloc_get(swigCPtr, this);
  }

  public void setLocalsize(int value) {
    arblibJNI.fmpz_multi_crt_struct_localsize_set(swigCPtr, this, value);
  }

  public int getLocalsize() {
    return arblibJNI.fmpz_multi_crt_struct_localsize_get(swigCPtr, this);
  }

  public void setTemp1loc(int value) {
    arblibJNI.fmpz_multi_crt_struct_temp1loc_set(swigCPtr, this, value);
  }

  public int getTemp1loc() {
    return arblibJNI.fmpz_multi_crt_struct_temp1loc_get(swigCPtr, this);
  }

  public void setTemp2loc(int value) {
    arblibJNI.fmpz_multi_crt_struct_temp2loc_set(swigCPtr, this, value);
  }

  public int getTemp2loc() {
    return arblibJNI.fmpz_multi_crt_struct_temp2loc_get(swigCPtr, this);
  }

  public void setGood(int value) {
    arblibJNI.fmpz_multi_crt_struct_good_set(swigCPtr, this, value);
  }

  public int getGood() {
    return arblibJNI.fmpz_multi_crt_struct_good_get(swigCPtr, this);
  }

  public fmpz_multi_crt_struct() {
    this(arblibJNI.new_fmpz_multi_crt_struct(), true);
  }

}
