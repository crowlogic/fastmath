/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class pthread_mutexattr_t {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected pthread_mutexattr_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(pthread_mutexattr_t obj) {
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
        arblibJNI.delete_pthread_mutexattr_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void set__size(String value) {
    arblibJNI.pthread_mutexattr_t___size_set(swigCPtr, this, value);
  }

  public String get__size() {
    return arblibJNI.pthread_mutexattr_t___size_get(swigCPtr, this);
  }

  public void set__align(int value) {
    arblibJNI.pthread_mutexattr_t___align_set(swigCPtr, this, value);
  }

  public int get__align() {
    return arblibJNI.pthread_mutexattr_t___align_get(swigCPtr, this);
  }

  public pthread_mutexattr_t() {
    this(arblibJNI.new_pthread_mutexattr_t(), true);
  }

}
