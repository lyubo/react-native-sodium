
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

JNIEXPORT jint JNICALL Java_org_libsodium_jni_SodiumJNI_crypto_1box_1keypair(JNIEnv *jenv, jclass jcls, jbyteArray jarg1, jbyteArray jarg2) {
  int result;

  (void)jenv;
  unsigned char *arg1 = (unsigned char *) (*jenv)->GetByteArrayElements(jenv, jarg1, 0);
  unsigned char *arg2 = (unsigned char *) (*jenv)->GetByteArrayElements(jenv, jarg2, 0);
  result = (int)crypto_box_keypair(arg1,arg2);
  (*jenv)->ReleaseByteArrayElements(jenv, jarg1, (jbyte *) arg1, 0);
  (*jenv)->ReleaseByteArrayElements(jenv, jarg2, (jbyte *) arg2, 0);
  return (jint)result;
}

#ifdef __cplusplus
}
#endif
