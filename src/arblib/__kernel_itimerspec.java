/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class __kernel_itimerspec {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected __kernel_itimerspec(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(__kernel_itimerspec obj) {
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
        arblibJNI.delete___kernel_itimerspec(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setIt_interval(__kernel_timespec value) {
    arblibJNI.__kernel_itimerspec_it_interval_set(swigCPtr, this, __kernel_timespec.getCPtr(value), value);
  }

  public __kernel_timespec getIt_interval() {
    long cPtr = arblibJNI.__kernel_itimerspec_it_interval_get(swigCPtr, this);
    return (cPtr == 0) ? null : new __kernel_timespec(cPtr, false);
  }

  public void setIt_value(__kernel_timespec value) {
    arblibJNI.__kernel_itimerspec_it_value_set(swigCPtr, this, __kernel_timespec.getCPtr(value), value);
  }

  public __kernel_timespec getIt_value() {
    long cPtr = arblibJNI.__kernel_itimerspec_it_value_get(swigCPtr, this);
    return (cPtr == 0) ? null : new __kernel_timespec(cPtr, false);
  }

  public __kernel_itimerspec() {
    this(arblibJNI.new___kernel_itimerspec(), true);
  }

}
