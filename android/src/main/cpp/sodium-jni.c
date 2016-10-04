
#include <jni.h>
#include "sodium.h"


#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_org_libsodium_jni_SodiumJNI_sodium_1init(JNIEnv *jenv, jclass jcls) {
  int result;
  result = (int)sodium_init();
  return (jint)result;
}

JNIEXPORT jstring JNICALL Java_org_libsodium_jni_SodiumJNI_sodium_1version_1string(JNIEnv *jenv, jclass jcls) {
  char *result = 0 ;

  (void)jenv;
  result = (char *)sodium_version_string();
  return (*jenv)->NewStringUTF(jenv, (const char *)result);
}

JNIEXPORT jint JNICALL Java_org_libsodium_jni_SodiumJNI_crypto_1box_1keypair(JNIEnv *jenv, jclass jcls, jbyteArray jpk, jbyteArray jsk) {
  int result;

  (void)jenv;
  unsigned char *pk = (unsigned char *) (*jenv)->GetByteArrayElements(jenv, jpk, 0);
  unsigned char *sk = (unsigned char *) (*jenv)->GetByteArrayElements(jenv, jsk, 0);
  result = (int)crypto_box_keypair(pk, sk);
  (*jenv)->ReleaseByteArrayElements(jenv, jpk, (jbyte *) pk, 0);
  (*jenv)->ReleaseByteArrayElements(jenv, jsk, (jbyte *) sk, 0);
  return (jint)result;
}

#ifdef __cplusplus
}
#endif
