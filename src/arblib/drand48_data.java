/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class drand48_data {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected drand48_data(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(drand48_data obj) {
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
        arblibJNI.delete_drand48_data(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void set__x(SWIGTYPE_p_unsigned_short value) {
    arblibJNI.drand48_data___x_set(swigCPtr, this, SWIGTYPE_p_unsigned_short.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_short get__x() {
    long cPtr = arblibJNI.drand48_data___x_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_short(cPtr, false);
  }

  public void set__old_x(SWIGTYPE_p_unsigned_short value) {
    arblibJNI.drand48_data___old_x_set(swigCPtr, this, SWIGTYPE_p_unsigned_short.getCPtr(value));
  }

  public SWIGTYPE_p_unsigned_short get__old_x() {
    long cPtr = arblibJNI.drand48_data___old_x_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_short(cPtr, false);
  }

  public void set__c(int value) {
    arblibJNI.drand48_data___c_set(swigCPtr, this, value);
  }

  public int get__c() {
    return arblibJNI.drand48_data___c_get(swigCPtr, this);
  }

  public void set__init(int value) {
    arblibJNI.drand48_data___init_set(swigCPtr, this, value);
  }

  public int get__init() {
    return arblibJNI.drand48_data___init_get(swigCPtr, this);
  }

  public void set__a(java.math.BigInteger value) {
    arblibJNI.drand48_data___a_set(swigCPtr, this, value);
  }

  public java.math.BigInteger get__a() {
    return arblibJNI.drand48_data___a_get(swigCPtr, this);
  }

  public drand48_data() {
    this(arblibJNI.new_drand48_data(), true);
  }

}
