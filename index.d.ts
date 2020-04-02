declare module "react-native-sodium" {
  export function sodium_version_string(): Promise<any>;

  //
  // Generating random data
  //
  /**
   * Returns an unpredictable value between 0 and 0xffffffff (included).
   */
  export function randombytes_random(): Promise<any>;

  /**
   * Returns an unpredictable value between 0 and upper_bound (excluded).
   * Unlike randombytes_random() % upper_bound, it guarantees a uniform distribution of
   * the possible output values even when upper_bound is not a power of 2. Note that an
   * upper_bound < 2 leaves only a single element to be chosen, namely 0.
   */
  export function randombytes_uniform(upper_bound: number): Promise<any>;

  /**
   * Create a nonce
   */
  export function randombytes_buf(size: number): Promise<any>;

  /**
   * This deallocates the global resources used by the pseudo-random number generator.
   * More specifically, when the /dev/urandom device is used, it closes the descriptor.
   * Explicitly calling this function is almost never required.
   */
  export function randombytes_close(): Promise<any>;

  /**
   * Reseeds the pseudo-random number generator, if it supports this operation.
   * Calling this function is not required with the default generator, even after a fork() call,
   * unless the descriptor for /dev/urandom was closed using randombytes_close().
   */
  export function randombytes_stir(): Promise<any>;

  //
  // Secret-key cryptography - Authenticated encryption
  //
  /**
   * Bytes of key on secret-key cryptography, authenticated encryption
   */
  export const crypto_secretbox_KEYBYTES: any;

  /**
   * Bytes of nonce on secret-key cryptography, authenticated encryption
   */
  export const crypto_secretbox_NONCEBYTES: any;

  /**
   * Bytes of the authentication on secret-key cryptography, authenticated encryption
   */
  export const crypto_secretbox_MACBYTES: any;

  /**
   * Creates a random key. It is equivalent to calling randombytes_buf() but improves code
   * clarity and can prevent misuse by ensuring that the provided key length is always be correct.
   */
  export function crypto_secretbox_keygen(): Promise<any>;

  /**
   * Encrypts a message, with a nonce and a key.
   */
  export function crypto_secretbox_easy(
    message: string,
    nonce: string,
    key: string
  ): Promise<any>;

  /**
   * Verifies and decrypts a ciphertext produced by crypto_secretbox_easy().
   * The nonce and the key have to match the used to encrypt and authenticate the message.
   */
  export function crypto_secretbox_open_easy(
    cipher: string,
    nonce: string,
    key: string
  ): Promise<any>;

  //
  // Secret-key cryptography - Authentication
  //
  /**
   * Bytes of key on secret-key cryptography, authentication
   */
  export const crypto_auth_KEYBYTES: any;

  /**
   * Bytes of the authentication on secret-key cryptography, authentication
   */
  export const crypto_auth_BYTES: any;

  /**
   * Computes a tag for the message and the key.
   */
  export function crypto_auth(inTag: string, key: string): Promise<any>;

  /**
   * Verifies that the tag stored at h is a valid tag for the message, and the key.
   */
  export function crypto_auth_verify(
    h: string,
    inTag: string,
    key: string
  ): Promise<any>;

  //
  // Public-key cryptography - Authenticated encryption
  //
  /**
   * Bytes of public key on public-key cryptography, authenticated encryption
   */
  export const crypto_box_PUBLICKEYBYTES: any;

  /**
   * Bytes of secret key on public-key cryptography, authenticated encryption
   */
  export const crypto_box_SECRETKEYBYTES: any;

  /**
   * Bytes of nonce on public-key cryptography, authenticated encryption
   */
  export const crypto_box_NONCEBYTES: any;

  /**
   * Bytes of the authentication on public-key cryptography, authenticated encryption
   */
  export const crypto_box_MACBYTES: any;

  /**
   *
   */
  export const crypto_box_ZEROBYTES: any;

  /**
   *
   */
  export const crypto_box_SEALBYTES: any;

  /**
   * Randomly generates a secret key and a corresponding public key.
   */
  export function crypto_box_keypair(): Promise<any>;

  /**
   * Encrypts a message, with a recipient's public key, a sender's secret key and a nonce.
   */
  export function crypto_box_easy(
    message: string,
    nonce: string,
    publicKey: string,
    secretKey: string
  ): Promise<any>;

  /**
   * Computes a shared secret key given a precalculated shared secret key.
   */
  export function crypto_box_easy_afternm(
    message: string,
    nonce: string,
    k: string
  ): Promise<any>;

  /**
   * Verifies and decrypts a ciphertext produced by crypto_box_easy().
   * The nonce has to match the nonce used to encrypt and authenticate the message.
   * Uses the public key of the sender that encrypted the message and the secret key
   * of the recipient that is willing to verify and decrypt it.
   */
  export function crypto_box_open_easy(
    cipher: string,
    nonce: string,
    publicKey: string,
    secretKey: string
  ): Promise<any>;

  /**
   * Computes a shared secret key given a precalculated shared secret key.
   */
  export function crypto_box_open_easy_afternm(
    cipher: string,
    nonce: string,
    k: string
  ): Promise<any>;

  /**
   * Computes a shared secret key given a public key pk and a secret key.
   */
  export function crypto_box_beforenm(
    publicKey: string,
    secretKey: string
  ): Promise<any>;

  /**
   * The key pair can be deterministically derived from a single key seed.
   */
  export function crypto_scalarmult_base(nonce: string): Promise<any>;

  //
  // Public-key cryptography - Sealed boxes
  //
  /**
   * Encrypts a message for a recipient's public key.
   */
  export function crypto_box_seal(
    message: string,
    publicKey: string
  ): Promise<any>;

  /**
   * Decrypts the ciphertext using the key pair.
   */
  export function crypto_box_seal_open(
    cipher: string,
    publicKey: string,
    secretKey: string
  ): Promise<any>;

  //
  // Public-key cryptography - Public-key signatures
  //
  /**
   * Bytes of public key on public-key cryptography, public-key signatures
   */
  export const crypto_sign_PUBLICKEYBYTES: any;

  /**
   * Bytes of secret key on public-key cryptography, public-key signatures
   */
  export const crypto_sign_SECRETKEYBYTES: any;

  /**
   * Bytes of single key seed on public-key cryptography, public-key signatures
   */
  export const crypto_sign_SEEDBYTES: any;

  /**
   * Bytes of the authentication on public-key cryptography, public-key signatures
   */
  export const crypto_sign_BYTES: any;

  /**
   * Signs the message using the secret key.
   */
  export function crypto_sign_detached(
    msg: string,
    secretKey: string
  ): Promise<any>;

  /**
   * Verifies that sig is a valid signature for the message using the signer's public key.
   */
  export function crypto_sign_verify_detached(
    sig: string,
    msg: string,
    publicKey: string
  ): Promise<any>;

  /**
   * Randomly generates a secret key and a corresponding public key.
   */
  export function crypto_sign_keypair(): Promise<any>;

  /**
   * Get key pair derived from a single key seed.
   */
  export function crypto_sign_seed_keypair(seed: string): Promise<any>;

  /**
   * Extracts the seed from the secret key.
   */
  export function crypto_sign_ed25519_sk_to_seed(
    secretKey: string
  ): Promise<any>;

  /**
   * Converts an Ed25519 public key to an X25519 public key.
   */
  export function crypto_sign_ed25519_pk_to_curve25519(
    publicKey: string
  ): Promise<any>;

  /**
   * Converts an Ed25519 secret key to an X25519 secret key
   */
  export function crypto_sign_ed25519_sk_to_curve25519(
    secretKey: string
  ): Promise<any>;

  /**
   * Extracts the seed from the secret key sk.
   */
  export function crypto_sign_ed25519_sk_to_pk(secretKey: string): Promise<any>;

  //
  // Password hashing
  //
  /**
   * Derives an key from a password and a salt whose fixed length is crypto_pwhash_SALTBYTES bytes.
   */
  export function crypto_pwhash(
    keylen: number,
    password: string,
    salt: string,
    opslimit: number,
    memlimit: number,
    algo: number
  ): Promise<any>;

  /**
   * Bytes of salt on password hashing, the pwhash* API.
   */
  export const crypto_pwhash_SALTBYTES: any;

  /**
   * Baseline for computations to perform on password hashing, the pwhash* API.
   */
  export const crypto_pwhash_OPSLIMIT_MODERATE: any;

  /**
   * Minimum numbers of CPU cycles to compute a key on password hashing, the pwhash* API.
   */
  export const crypto_pwhash_OPSLIMIT_MIN: any;

  /**
   * Maximum numbers of CPU cycles to compute a key on password hashing, the pwhash* API.
   */
  export const crypto_pwhash_OPSLIMIT_MAX: any;

  /**
   * Baseline for memory on password hashing, the pwhash* API.
   */
  export const crypto_pwhash_MEMLIMIT_MODERATE: any;

  /**
   * Minimum memory allowed to compute a key on password hashing, the pwhash* API.
   */
  export const crypto_pwhash_MEMLIMIT_MIN: any;

  /**
   * Maximum memory allowed to compute a key on password hashing, the pwhash* API.
   */
  export const crypto_pwhash_MEMLIMIT_MAX: any;

  /**
   * Tthe currently recommended algorithm, which can change from one version of libsodium to another.
   * On password hashing, the pwhash* API.
   */
  export const crypto_pwhash_ALG_DEFAULT: any;

  /**
   * Version 1.3 of the Argon2i algorithm.
   */
  export const crypto_pwhash_ALG_ARGON2I13: any;

  /**
   * Version 1.3 of the Argon2id algorithm, available since libsodium 1.0.13.
   */
  export const crypto_pwhash_ALG_ARGON2ID13: any;
}
