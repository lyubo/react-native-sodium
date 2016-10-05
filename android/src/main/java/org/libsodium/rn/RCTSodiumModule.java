package org.libsodium.rn;

/**
* Created by Lyubomir Ivanov on 21/09/16.
*/

import android.util.Base64;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;

import org.libsodium.jni.Sodium;

public class RCTSodiumModule extends ReactContextBaseJavaModule {


  public RCTSodiumModule(ReactApplicationContext reactContext) {
    super(reactContext);
    Sodium.loadLibrary();
  }

  @Override
  public String getName() {
    return "Sodium";
  }


  // ***************************************************************************
  // * Sodium-specific functions
  // ***************************************************************************
  @ReactMethod
  public void sodium_version_string(final Promise p) {
    try {
      p.resolve(Sodium.sodium_version_string());
    }
    catch (Throwable t) {
      p.reject(t);
    }
  }


  // ***************************************************************************
  // * Random data generation
  // ***************************************************************************
  @ReactMethod
  public void randombytes_random(final Promise p) {
    // RN0.34: Long can't be passed through the bridge (int and double only)
    p.resolve(Long.valueOf(Sodium.randombytes_random()).doubleValue());
  }

  @ReactMethod
  public void randombytes_uniform(Double upper_bound, final Promise p) {
    // RN0.34: Long can't be passed through the bridge (int and double only)
    p.resolve(Long.valueOf(Sodium.randombytes_uniform(upper_bound.longValue())).doubleValue());
  }

  @ReactMethod
  public void randombytes_buf(int size, final Promise p) {
    try {
      byte[] buf = new byte[size];
      Sodium.randombytes_buf(buf, size);
      p.resolve(Base64.encodeToString(buf,Base64.NO_WRAP));
    }
    catch (Throwable t) {
      p.reject(t);
    }
  }

  @ReactMethod
  public void randombytes_close() {
    Sodium.randombytes_close();
  }

  @ReactMethod
  public void randombytes_stir() {
    Sodium.randombytes_stir();
  }


  // ***************************************************************************
  // * Public-key cryptography - authenticated encryption
  // ***************************************************************************
  @ReactMethod
  public void crypto_box_keypair(final Promise p){
    try {
      byte[] pk = new byte[Sodium.crypto_box_PUBLICKEYBYTES];
      byte[] sk = new byte[Sodium.crypto_box_SECRETKEYBYTES];

      Sodium.crypto_box_keypair(pk, sk);

      WritableNativeMap result = new WritableNativeMap();
      result.putString("pk",Base64.encodeToString(pk,Base64.NO_WRAP));
      result.putString("sk",Base64.encodeToString(sk,Base64.NO_WRAP));
      p.resolve(result);
    }
    catch (Throwable t) {
      p.reject(t);
    }
  }

}
