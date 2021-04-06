/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class dirichlet_prime_group_struct {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected dirichlet_prime_group_struct(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(dirichlet_prime_group_struct obj) {
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
        arblibJNI.delete_dirichlet_prime_group_struct(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setP(SWIGTYPE_p_ulong value) {
    arblibJNI.dirichlet_prime_group_struct_p_set(swigCPtr, this, SWIGTYPE_p_ulong.getCPtr(value));
  }

  public SWIGTYPE_p_ulong getP() {
    return new SWIGTYPE_p_ulong(arblibJNI.dirichlet_prime_group_struct_p_get(swigCPtr, this), true);
  }

  public void setE(int value) {
    arblibJNI.dirichlet_prime_group_struct_e_set(swigCPtr, this, value);
  }

  public int getE() {
    return arblibJNI.dirichlet_prime_group_struct_e_get(swigCPtr, this);
  }

  public void setPe(SWIGTYPE_p_nmod_t value) {
    arblibJNI.dirichlet_prime_group_struct_pe_set(swigCPtr, this, SWIGTYPE_p_nmod_t.getCPtr(value));
  }

  public SWIGTYPE_p_nmod_t getPe() {
    return new SWIGTYPE_p_nmod_t(arblibJNI.dirichlet_prime_group_struct_pe_get(swigCPtr, this), true);
  }

  public void setPhi(SWIGTYPE_p_nmod_t value) {
    arblibJNI.dirichlet_prime_group_struct_phi_set(swigCPtr, this, SWIGTYPE_p_nmod_t.getCPtr(value));
  }

  public SWIGTYPE_p_nmod_t getPhi() {
    return new SWIGTYPE_p_nmod_t(arblibJNI.dirichlet_prime_group_struct_phi_get(swigCPtr, this), true);
  }

  public void setG(SWIGTYPE_p_ulong value) {
    arblibJNI.dirichlet_prime_group_struct_g_set(swigCPtr, this, SWIGTYPE_p_ulong.getCPtr(value));
  }

  public SWIGTYPE_p_ulong getG() {
    return new SWIGTYPE_p_ulong(arblibJNI.dirichlet_prime_group_struct_g_get(swigCPtr, this), true);
  }

  public void setDlog(SWIGTYPE_p_dlog_precomp_struct value) {
    arblibJNI.dirichlet_prime_group_struct_dlog_set(swigCPtr, this, SWIGTYPE_p_dlog_precomp_struct.getCPtr(value));
  }

  public SWIGTYPE_p_dlog_precomp_struct getDlog() {
    long cPtr = arblibJNI.dirichlet_prime_group_struct_dlog_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_dlog_precomp_struct(cPtr, false);
  }

  public dirichlet_prime_group_struct() {
    this(arblibJNI.new_dirichlet_prime_group_struct(), true);
  }

}
