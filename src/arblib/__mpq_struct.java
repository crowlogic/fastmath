/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class __mpq_struct {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected __mpq_struct(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(__mpq_struct obj) {
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
        arblibJNI.delete___mpq_struct(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void set_mp_num(__mpz_struct value) {
    arblibJNI.__mpq_struct__mp_num_set(swigCPtr, this, __mpz_struct.getCPtr(value), value);
  }

  public __mpz_struct get_mp_num() {
    long cPtr = arblibJNI.__mpq_struct__mp_num_get(swigCPtr, this);
    return (cPtr == 0) ? null : new __mpz_struct(cPtr, false);
  }

  public void set_mp_den(__mpz_struct value) {
    arblibJNI.__mpq_struct__mp_den_set(swigCPtr, this, __mpz_struct.getCPtr(value), value);
  }

  public __mpz_struct get_mp_den() {
    long cPtr = arblibJNI.__mpq_struct__mp_den_get(swigCPtr, this);
    return (cPtr == 0) ? null : new __mpz_struct(cPtr, false);
  }

  public __mpq_struct() {
    this(arblibJNI.new___mpq_struct(), true);
  }

}
