
package org.libsodium.jni;

public class Sodium extends  SodiumJNI {

    public static final int SHA256BYTES = 32;
    public static final int SHA512BYTES = 64;
    public static final int BLAKE2B_OUTBYTES = 64;
    public static final int PUBLICKEY_BYTES = 32;
    public static final int SECRETKEY_BYTES = 32;
    public static final int NONCE_BYTES = 24;
    public static final int ZERO_BYTES = 32;
    public static final int BOXZERO_BYTES = 16;
    public static final int SCALAR_BYTES = 32;
    public static final int XSALSA20_POLY1305_SECRETBOX_KEYBYTES = 32;
    public static final int XSALSA20_POLY1305_SECRETBOX_NONCEBYTES = 24;
    public static final int SIGNATURE_BYTES = 64;
    public static final int AEAD_CHACHA20_POLY1305_KEYBYTES = 32;
    public static final int AEAD_CHACHA20_POLY1305_NPUBBYTES = 8;
    public static final int AEAD_CHACHA20_POLY1305_ABYTES = 8;

    public final static void loadLibrary() {
        System.loadLibrary("sodium-jni");
        sodium_init();
    }

}
