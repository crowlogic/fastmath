/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class ecm_s {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ecm_s(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ecm_s obj) {
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
        arblibJNI.delete_ecm_s(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setT(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_t_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getT() {
    long cPtr = arblibJNI.ecm_s_t_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setU(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_u_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getU() {
    long cPtr = arblibJNI.ecm_s_u_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setV(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_v_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getV() {
    long cPtr = arblibJNI.ecm_s_v_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setW(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_w_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getW() {
    long cPtr = arblibJNI.ecm_s_w_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setX(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_x_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getX() {
    long cPtr = arblibJNI.ecm_s_x_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setZ(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_z_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getZ() {
    long cPtr = arblibJNI.ecm_s_z_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setA24(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_a24_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getA24() {
    long cPtr = arblibJNI.ecm_s_a24_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setNinv(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_ninv_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getNinv() {
    long cPtr = arblibJNI.ecm_s_ninv_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setOne(SWIGTYPE_p_unsigned_long value) {
    arblibJNI.ecm_s_one_set(swigCPtr, this, SWIGTYPE_p_unsigned_long.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_long getOne() {
    long cPtr = arblibJNI.ecm_s_one_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long(cPtr, false);
  }

  public void setGCD_table(SWIGTYPE_p_unsigned_char value) {
    arblibJNI.ecm_s_GCD_table_set(swigCPtr, this, SWIGTYPE_p_unsigned_char.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_char getGCD_table() {
    long cPtr = arblibJNI.ecm_s_GCD_table_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
  }

  public void setPrime_table(SWIGTYPE_p_p_unsigned_char value) {
    arblibJNI.ecm_s_prime_table_set(swigCPtr, this, SWIGTYPE_p_p_unsigned_char.getCPtr(value));
  }

  public SWIGTYPE_p_p_unsigned_char getPrime_table() {
    long cPtr = arblibJNI.ecm_s_prime_table_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_p_unsigned_char(cPtr, false);
  }

  public void setN_size(long value) {
    arblibJNI.ecm_s_n_size_set(swigCPtr, this, value);
  }

  public long getN_size() {
    return arblibJNI.ecm_s_n_size_get(swigCPtr, this);
  }

  public void setNormbits(long value) {
    arblibJNI.ecm_s_normbits_set(swigCPtr, this, value);
  }

  public long getNormbits() {
    return arblibJNI.ecm_s_normbits_get(swigCPtr, this);
  }

  public ecm_s() {
    this(arblibJNI.new_ecm_s(), true);
  }

}
