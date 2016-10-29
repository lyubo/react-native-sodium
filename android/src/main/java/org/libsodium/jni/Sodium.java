
package org.libsodium.jni;

public class Sodium extends  SodiumJNI {

  public static final int crypto_box_PUBLICKEYBYTES = 32;
  public static final int crypto_box_SECRETKEYBYTES = 32;
  public static final int crypto_auth_KEYBYTES = 32;
  public static final int crypto_auth_BYTES = 32;

  public final static void loadLibrary() {
    System.loadLibrary("sodium-jni");
    sodium_init();
  }

}
