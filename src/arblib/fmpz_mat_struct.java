/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class fmpz_mat_struct {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected fmpz_mat_struct(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(fmpz_mat_struct obj) {
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
        arblibJNI.delete_fmpz_mat_struct(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setEntries(SWIGTYPE_p_long value) {
    arblibJNI.fmpz_mat_struct_entries_set(swigCPtr, this, SWIGTYPE_p_long.getCPtr(value));
  }

  public SWIGTYPE_p_long getEntries() {
    long cPtr = arblibJNI.fmpz_mat_struct_entries_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_long(cPtr, false);
  }

  public void setR(int value) {
    arblibJNI.fmpz_mat_struct_r_set(swigCPtr, this, value);
  }

  public int getR() {
    return arblibJNI.fmpz_mat_struct_r_get(swigCPtr, this);
  }

  public void setC(int value) {
    arblibJNI.fmpz_mat_struct_c_set(swigCPtr, this, value);
  }

  public int getC() {
    return arblibJNI.fmpz_mat_struct_c_get(swigCPtr, this);
  }

  public void setRows(SWIGTYPE_p_p_long value) {
    arblibJNI.fmpz_mat_struct_rows_set(swigCPtr, this, SWIGTYPE_p_p_long.getCPtr(value));
  }

  public SWIGTYPE_p_p_long getRows() {
    long cPtr = arblibJNI.fmpz_mat_struct_rows_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_p_long(cPtr, false);
  }

  public fmpz_mat_struct() {
    this(arblibJNI.new_fmpz_mat_struct(), true);
  }

}
