package org.libsodium.jni;

public class SodiumJNI {
  public final static native int sodium_init();
  public final static native String sodium_version_string();

  public final static native long randombytes_random();
  public final static native long randombytes_uniform(long upper_bound);
  public final static native void randombytes_buf(byte[] buf, int size);
  public final static native int randombytes_close();
  public final static native void randombytes_stir();

  public final static native int crypto_box_keypair(byte[] pk, byte[] sk);

}
