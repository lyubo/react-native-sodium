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
  public final static native void crypto_secretbox_keygen(byte[] key);
  public final static native int crypto_secretbox_easy(byte[] c, final byte[] m, final long mlen, final byte[] n, final byte[] k);
  public final static native int crypto_secretbox_open_easy(byte[] m, final byte[] c, final long clen,  final byte[] n, final byte[] k);

  public final static native int crypto_auth_keybytes();
  public final static native int crypto_auth_bytes();
  public final static native void crypto_auth_keygen(byte[] key);
  public final static native int crypto_auth(byte[] out, final byte[] in, final long inlen,  final byte[] k);
  public final static native int crypto_auth_verify(final byte[] h, final byte[] in, final long inlen, final byte[] k);

  public final static native int crypto_box_publickeybytes();
  public final static native int crypto_box_secretkeybytes();
  public final static native int crypto_box_beforenmbytes();
  public final static native int crypto_box_seedbytes();
  public final static native int crypto_box_noncebytes();
  public final static native int crypto_box_macbytes();
  public final static native int crypto_box_zerobytes();
  public final static native int crypto_box_boxzerobytes();
  public final static native int crypto_box_sealbytes();
  public final static native int crypto_box_keypair(byte[] pk, byte[] sk);
  public final static native int crypto_box_easy(byte[] c, final byte[] m, final long mlen, final byte[] n, final byte[] pk, final byte[] sk);
  public final static native int crypto_box_open_easy(byte[] m, final byte[] c, final long clen,  final byte[] n, final byte[] pk, final byte[] sk);

  public final static native int crypto_box_beforenm(byte[] s, final byte[] pk, final byte[] sk);
  public final static native int crypto_box_easy_afternm(byte[] c, byte[] m, long mlen, byte [] n, byte[] k);
  public final static native int crypto_box_open_easy_afternm(byte[] m, byte[] c, long clen, byte[] n, byte[] k);

  public final static native int crypto_box_seal(byte[] c, byte[] m, long mlen, byte[] pk);
  public final static native int crypto_box_seal_open(byte[] m, byte[] c, long clen, byte[] pk, byte[] sk);

  public final static native int crypto_pwhash(byte[] out, final long olen, final byte[] password, final long plen, byte[] salt, long opslimit, long memlimit, int algo);
  public final static native int crypto_pwhash_salt_bytes();
  public final static native int crypto_pwhash_opslimit_moderate();
  public final static native int crypto_pwhash_opslimit_min();
  public final static native int crypto_pwhash_opslimit_max();
  public final static native int crypto_pwhash_memlimit_moderate();
  public final static native int crypto_pwhash_memlimit_min();
  public final static native int crypto_pwhash_memlimit_max();
  public final static native int crypto_pwhash_algo_default();
  public final static native int crypto_pwhash_algo_argon2i13();
  public final static native int crypto_pwhash_algo_argon2id13();

  public final static native int crypto_scalarmult_base(byte[] q, final byte[] n);

  public final static native int crypto_sign_publickeybytes();
  public final static native int crypto_sign_secretkeybytes();
  public final static native int crypto_sign_seedbytes();
  public final static native int crypto_sign_bytes();
  public final static native int crypto_sign_detached(byte[] sig, final byte[] msg, final int msg_len, final byte[] sk);
  public final static native int crypto_sign_verify_detached(byte[] sig, byte[] msg, long msg_len, final byte[] pk);
  public final static native int crypto_sign_keypair(byte[] pk, byte[] sk);
  public final static native int crypto_sign_seed_keypair(byte[] pk, byte[] sk, final byte[] seed);
  public final static native int crypto_sign_ed25519_sk_to_seed(byte[] seed, final byte[] sk);
  public final static native int crypto_sign_ed25519_pk_to_curve25519(byte[] curve25519_pk, final byte[] ed25519_pk);
  public final static native int crypto_sign_ed25519_sk_to_curve25519(byte[] curve25519_sk, final byte[] ed25519_sk);
  public final static native int crypto_sign_ed25519_sk_to_pk(byte[] sk, byte[] pk);

  public final static native int base64_variant_ORIGINAL();
  public final static native int base64_variant_VARIANT_ORIGINAL_NO_PADDING();
  public final static native int base64_variant_VARIANT_URLSAFE();
  public final static native int base64_variant_VARIANT_URLSAFE_NO_PADDING();

  public final static native char sodium_bin2base64(byte[] b64, final int b64_maxlen, final byte[] bin, final int bin_len, final int variant);
  public final static native int sodium_base642bin(final byte[] bin, int bin_maxlen, final byte[] b64, final int b64_len, final byte[] ignore, final int[] bin_len, final byte[] b64_end, final int variant);
  public final static native char sodium_bin2hex(byte[] hex, int hex_maxlen, byte[] bin, final int bin_len);
  public final static native int sodium_hex2bin(byte[] bin, final int bin_maxlen, final byte[] hex, final int hex_len, final byte[] ignore, final int[] bin_len, final byte[] hex_end);
  public final static native int sodium_base64_encoded_len(final int bin_len, final int variant);
}
