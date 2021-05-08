/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package arblib;

public class _nmod_poly_multi_crt_prog_instr {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected _nmod_poly_multi_crt_prog_instr(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(_nmod_poly_multi_crt_prog_instr obj) {
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
        arblibJNI.delete__nmod_poly_multi_crt_prog_instr(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setA_idx(int value) {
    arblibJNI._nmod_poly_multi_crt_prog_instr_a_idx_set(swigCPtr, this, value);
  }

  public int getA_idx() {
    return arblibJNI._nmod_poly_multi_crt_prog_instr_a_idx_get(swigCPtr, this);
  }

  public void setB_idx(int value) {
    arblibJNI._nmod_poly_multi_crt_prog_instr_b_idx_set(swigCPtr, this, value);
  }

  public int getB_idx() {
    return arblibJNI._nmod_poly_multi_crt_prog_instr_b_idx_get(swigCPtr, this);
  }

  public void setC_idx(int value) {
    arblibJNI._nmod_poly_multi_crt_prog_instr_c_idx_set(swigCPtr, this, value);
  }

  public int getC_idx() {
    return arblibJNI._nmod_poly_multi_crt_prog_instr_c_idx_get(swigCPtr, this);
  }

  public void setIdem(nmod_poly_struct value) {
    arblibJNI._nmod_poly_multi_crt_prog_instr_idem_set(swigCPtr, this, nmod_poly_struct.getCPtr(value), value);
  }

  public nmod_poly_struct getIdem() {
    long cPtr = arblibJNI._nmod_poly_multi_crt_prog_instr_idem_get(swigCPtr, this);
    return (cPtr == 0) ? null : new nmod_poly_struct(cPtr, false);
  }

  public void setModulus(nmod_poly_struct value) {
    arblibJNI._nmod_poly_multi_crt_prog_instr_modulus_set(swigCPtr, this, nmod_poly_struct.getCPtr(value), value);
  }

  public nmod_poly_struct getModulus() {
    long cPtr = arblibJNI._nmod_poly_multi_crt_prog_instr_modulus_get(swigCPtr, this);
    return (cPtr == 0) ? null : new nmod_poly_struct(cPtr, false);
  }

  public _nmod_poly_multi_crt_prog_instr() {
    this(arblibJNI.new__nmod_poly_multi_crt_prog_instr(), true);
  }

}
