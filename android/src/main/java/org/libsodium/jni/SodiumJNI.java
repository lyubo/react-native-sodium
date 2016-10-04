package org.libsodium.jni;

public class SodiumJNI {
  public final static native int sodium_init();
  public final static native String sodium_version_string();

  public final static native int crypto_box_keypair(byte[] pk, byte[] sk);

}
