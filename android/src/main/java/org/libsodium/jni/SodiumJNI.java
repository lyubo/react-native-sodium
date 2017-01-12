package org.libsodium.jni;

public class SodiumJNI {
  public final static native int sodium_init();
  public final static native String sodium_version_string();

  public final static native long randombytes_random();
  public final static native long randombytes_uniform(long upper_bound);
  public final static native void randombytes_buf(byte[] buf, int size);
  public final static native int randombytes_close();
  public final static native void randombytes_stir();

  public final static native int crypto_secretbox_keybytes();
  public final static native int crypto_secretbox_noncebytes();
  public final static native int crypto_secretbox_macbytes();
  public final static native int crypto_secretbox_easy(byte[] c, final byte[] m, final long mlen, final byte[] n, final byte[] k);
  public final static native int crypto_secretbox_open_easy(byte[] m, final byte[] c, final long clen,  final byte[] n, final byte[] k);

  public final static native int crypto_auth_keybytes();
  public final static native int crypto_auth_bytes();
  public final static native int crypto_auth(byte[] out, final byte[] in, final long inlen,  final byte[] k);
  public final static native int crypto_auth_verify(final byte[] h, final byte[] in, final long inlen, final byte[] k);

  public final static native int crypto_box_publickeybytes();
  public final static native int crypto_box_secretkeybytes();
  public final static native int crypto_box_noncebytes();
  public final static native int crypto_box_macbytes();
  public final static native int crypto_box_zerobytes();
  public final static native int crypto_box_keypair(byte[] pk, byte[] sk);
  public final static native int crypto_box_easy(byte[] c, final byte[] m, final long mlen, final byte[] n, final byte[] pk, final byte[] sk);
  public final static native int crypto_box_open_easy(byte[] m, final byte[] c, final long clen,  final byte[] n, final byte[] pk, final byte[] sk);
}
