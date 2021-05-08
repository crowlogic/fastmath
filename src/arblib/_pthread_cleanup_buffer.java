/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class _pthread_cleanup_buffer {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected _pthread_cleanup_buffer(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(_pthread_cleanup_buffer obj) {
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
        arblibJNI.delete__pthread_cleanup_buffer(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void set__routine(SWIGTYPE_p_f_p_void__void value) {
    arblibJNI._pthread_cleanup_buffer___routine_set(swigCPtr, this, SWIGTYPE_p_f_p_void__void.getCPtr(value));
  }

  public SWIGTYPE_p_f_p_void__void get__routine() {
    long cPtr = arblibJNI._pthread_cleanup_buffer___routine_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_p_void__void(cPtr, false);
  }

  public void set__arg(SWIGTYPE_p_void value) {
    arblibJNI._pthread_cleanup_buffer___arg_set(swigCPtr, this, SWIGTYPE_p_void.getCPtr(value));
  }

  public SWIGTYPE_p_void get__arg() {
    long cPtr = arblibJNI._pthread_cleanup_buffer___arg_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
  }

  public void set__canceltype(int value) {
    arblibJNI._pthread_cleanup_buffer___canceltype_set(swigCPtr, this, value);
  }

  public int get__canceltype() {
    return arblibJNI._pthread_cleanup_buffer___canceltype_get(swigCPtr, this);
  }

  public void set__prev(_pthread_cleanup_buffer value) {
    arblibJNI._pthread_cleanup_buffer___prev_set(swigCPtr, this, _pthread_cleanup_buffer.getCPtr(value), value);
  }

  public _pthread_cleanup_buffer get__prev() {
    long cPtr = arblibJNI._pthread_cleanup_buffer___prev_get(swigCPtr, this);
    return (cPtr == 0) ? null : new _pthread_cleanup_buffer(cPtr, false);
  }

  public _pthread_cleanup_buffer() {
    this(arblibJNI.new__pthread_cleanup_buffer(), true);
  }

}
