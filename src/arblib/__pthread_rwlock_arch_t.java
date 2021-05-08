/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class __pthread_rwlock_arch_t {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected __pthread_rwlock_arch_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(__pthread_rwlock_arch_t obj) {
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
        arblibJNI.delete___pthread_rwlock_arch_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void set__readers(long value) {
    arblibJNI.__pthread_rwlock_arch_t___readers_set(swigCPtr, this, value);
  }

  public long get__readers() {
    return arblibJNI.__pthread_rwlock_arch_t___readers_get(swigCPtr, this);
  }

  public void set__writers(long value) {
    arblibJNI.__pthread_rwlock_arch_t___writers_set(swigCPtr, this, value);
  }

  public long get__writers() {
    return arblibJNI.__pthread_rwlock_arch_t___writers_get(swigCPtr, this);
  }

  public void set__wrphase_futex(long value) {
    arblibJNI.__pthread_rwlock_arch_t___wrphase_futex_set(swigCPtr, this, value);
  }

  public long get__wrphase_futex() {
    return arblibJNI.__pthread_rwlock_arch_t___wrphase_futex_get(swigCPtr, this);
  }

  public void set__writers_futex(long value) {
    arblibJNI.__pthread_rwlock_arch_t___writers_futex_set(swigCPtr, this, value);
  }

  public long get__writers_futex() {
    return arblibJNI.__pthread_rwlock_arch_t___writers_futex_get(swigCPtr, this);
  }

  public void set__pad3(long value) {
    arblibJNI.__pthread_rwlock_arch_t___pad3_set(swigCPtr, this, value);
  }

  public long get__pad3() {
    return arblibJNI.__pthread_rwlock_arch_t___pad3_get(swigCPtr, this);
  }

  public void set__pad4(long value) {
    arblibJNI.__pthread_rwlock_arch_t___pad4_set(swigCPtr, this, value);
  }

  public long get__pad4() {
    return arblibJNI.__pthread_rwlock_arch_t___pad4_get(swigCPtr, this);
  }

  public void set__flags(short value) {
    arblibJNI.__pthread_rwlock_arch_t___flags_set(swigCPtr, this, value);
  }

  public short get__flags() {
    return arblibJNI.__pthread_rwlock_arch_t___flags_get(swigCPtr, this);
  }

  public void set__shared(short value) {
    arblibJNI.__pthread_rwlock_arch_t___shared_set(swigCPtr, this, value);
  }

  public short get__shared() {
    return arblibJNI.__pthread_rwlock_arch_t___shared_get(swigCPtr, this);
  }

  public void set__rwelision(byte value) {
    arblibJNI.__pthread_rwlock_arch_t___rwelision_set(swigCPtr, this, value);
  }

  public byte get__rwelision() {
    return arblibJNI.__pthread_rwlock_arch_t___rwelision_get(swigCPtr, this);
  }

  public void set__pad2(short value) {
    arblibJNI.__pthread_rwlock_arch_t___pad2_set(swigCPtr, this, value);
  }

  public short get__pad2() {
    return arblibJNI.__pthread_rwlock_arch_t___pad2_get(swigCPtr, this);
  }

  public void set__cur_writer(int value) {
    arblibJNI.__pthread_rwlock_arch_t___cur_writer_set(swigCPtr, this, value);
  }

  public int get__cur_writer() {
    return arblibJNI.__pthread_rwlock_arch_t___cur_writer_get(swigCPtr, this);
  }

  public __pthread_rwlock_arch_t() {
    this(arblibJNI.new___pthread_rwlock_arch_t(), true);
  }

}
