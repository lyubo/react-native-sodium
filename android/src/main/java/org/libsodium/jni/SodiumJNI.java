package org.libsodium.jni;

public class SodiumJNI {
  public final static native int sodium_init();
  public final static native String sodium_version_string();

  public final static native long randombytes_random();
  public final static native long randombytes_uniform(long upper_bound);
  public final static native void randombytes_buf(byte[] buf, int size);
  public final static native int  randombytes_close();
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

  public final static native int crypto_box_beforenm(byte[] s, final byte[] pk, final byte[] sk);
  public final static native int crypto_box_easy_afternm(byte[] c, byte[] m, long mlen, byte [] n, byte[] k);
  public final static native int crypto_box_open_easy_afternm(byte[] m, byte[] c, long clen, byte[] n, byte[] k);

  public final static native int crypto_sign_publickeybytes();
  public final static native int crypto_sign_secretkeybytes();
  public final static native int crypto_sign_seedbytes();
  public final static native int crypto_sign_bytes();
  public final static native int crypto_sign_detached(byte[] sig, final byte[] msg, final int msg_len, final byte[] sk);
  public final static native int crypto_sign_verify_detached(byte[] sig, byte[] msg, long msg_len, final byte[] pk);
  public final static native int crypto_sign_seed_keypair(byte[] pk, byte[] sk, final byte[] seed);
  public final static native int crypto_sign_ed25519_sk_to_seed(byte[] seed, final byte[] sk);
  public final static native int crypto_sign_ed25519_pk_to_curve25519(byte[] curve25519_pk, final byte[] ed25519_pk);
  public final static native int crypto_sign_ed25519_sk_to_curve25519(byte[] curve25519_sk, final byte[] ed25519_sk);
}
