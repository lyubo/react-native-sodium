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

  static final String ERR_BAD_KEYSIZE = "BAD_KEYSIZE";
  static final String ERR_BAD_AUTHENTICATOR = "BAD_AUTHENTICATOR";

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
  // * Secret-key cryptography - authentication
  // ***************************************************************************
  @ReactMethod
  public void crypto_auth(String in, String k, final Promise p){
    try {
      byte[] out = new byte[Sodium.crypto_auth_BYTES];
      byte[] inb = Base64.decode(in, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_auth_KEYBYTES) throw new  RuntimeException(ERR_BAD_KEYSIZE);

      Sodium.crypto_auth(out, inb, inb.length, kb);
      p.resolve(Base64.encodeToString(out,Base64.NO_WRAP));
    }
    catch (Throwable t) {
      p.reject(t);
    }
  }

  @ReactMethod
  public void crypto_auth_verify(String h, String in, String k, final Promise p){
    try {
      byte[] inb = Base64.decode(in, Base64.NO_WRAP);
      byte[] hb = Base64.decode(h, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_auth_KEYBYTES) throw new RuntimeException(ERR_BAD_KEYSIZE);
      if (hb.length != Sodium.crypto_auth_BYTES) throw new RuntimeException(ERR_BAD_AUTHENTICATOR);

      int result = Sodium.crypto_auth_verify(hb, inb, inb.length, kb);
      p.resolve(result);
    }
    catch (Throwable t) {
      p.reject(t);
    }
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
