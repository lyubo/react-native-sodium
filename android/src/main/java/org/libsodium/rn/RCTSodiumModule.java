package org.libsodium.rn;

/**
* Created by Lyubomir Ivanov on 21/09/16.
*/

import java.util.Map;
import java.util.HashMap;
import android.util.Base64;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;

import org.libsodium.jni.Sodium;

public class RCTSodiumModule extends ReactContextBaseJavaModule {

  static final String ESODIUM = "ESODIUM";
  static final String ERR_BAD_KEY = "BAD_KEY";
  static final String ERR_BAD_MAC = "BAD_MAC";
  static final String ERR_BAD_MSG = "BAD_MSG";
  static final String ERR_BAD_NONCE = "BAD_NONCE";
  static final String ERR_BAD_SEED = "BAD_SEED";
  static final String ERR_BAD_SIG = "BAD_SIG";
  static final String ERR_FAILURE = "FAILURE";

  public RCTSodiumModule(ReactApplicationContext reactContext) {
    super(reactContext);
    Sodium.loadLibrary();
  }

  @Override
  public String getName() {
    return "Sodium";
  }

  @Override
  public Map<String, Object> getConstants() {
     final Map<String, Object> constants = new HashMap<>();
     constants.put("crypto_secretbox_KEYBYTES", Sodium.crypto_secretbox_keybytes());
     constants.put("crypto_secretbox_NONCEBYTES", Sodium.crypto_secretbox_noncebytes());
     constants.put("crypto_secretbox_MACBYTES", Sodium.crypto_secretbox_macbytes());
     constants.put("crypto_auth_KEYBYTES", Sodium.crypto_auth_keybytes());
     constants.put("crypto_auth_BYTES", Sodium.crypto_auth_bytes());
     constants.put("crypto_box_PUBLICKEYBYTES", Sodium.crypto_box_publickeybytes());
     constants.put("crypto_box_SECRETKEYBYTES", Sodium.crypto_box_secretkeybytes());
     constants.put("crypto_box_NONCEBYTES", Sodium.crypto_box_noncebytes());
     constants.put("crypto_box_MACBYTES", Sodium.crypto_box_macbytes());
     constants.put("crypto_box_ZEROBYTES", Sodium.crypto_box_zerobytes());
     constants.put("crypto_sign_PUBLICKEYBYTES", Sodium.crypto_sign_publickeybytes());
     constants.put("crypto_sign_SECRETKEYBYTES", Sodium.crypto_sign_secretkeybytes());
     constants.put("crypto_sign_SEEDBYTES", Sodium.crypto_sign_seedbytes());
     constants.put("crypto_sign_BYTES", Sodium.crypto_sign_bytes());
     return constants;
  }

  // ***************************************************************************
  // * Sodium-specific functions
  // ***************************************************************************
  @ReactMethod
  public void sodium_version_string(final Promise p) {
    p.resolve(Sodium.sodium_version_string());
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
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void randombytes_close(final Promise p) {
    int result = Sodium.randombytes_close();
    if (result == 0) p.resolve(0); else p.reject(ESODIUM,ERR_FAILURE);
  }

  @ReactMethod
  public void randombytes_stir(final Promise p) {
    Sodium.randombytes_stir();
    p.resolve(0);
  }


  // ***************************************************************************
  // * Secret-key cryptography - authenticated encryption
  // ***************************************************************************
  @ReactMethod
  public void crypto_secretbox_easy(final String m, final String n, final String k, final Promise p) {
    try {
      byte[] mb = Base64.decode(m, Base64.NO_WRAP);
      byte[] nb = Base64.decode(n, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_secretbox_keybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (nb.length != Sodium.crypto_secretbox_noncebytes())
        p.reject(ESODIUM,ERR_BAD_NONCE);
      else {
        byte[] cb = new byte[mb.length + Sodium.crypto_secretbox_macbytes()];
        int result = Sodium.crypto_secretbox_easy(cb, mb, mb.length, nb, kb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(cb,Base64.NO_WRAP));
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void crypto_secretbox_open_easy(final String c, final String n, final String k, final Promise p) {
    try {
      byte[] cb = Base64.decode(c, Base64.NO_WRAP);
      byte[] nb = Base64.decode(n, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_secretbox_keybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (nb.length != Sodium.crypto_box_noncebytes())
        p.reject(ESODIUM,ERR_BAD_NONCE);
      else if (cb.length <=  Sodium.crypto_box_macbytes())
        p.reject(ESODIUM,ERR_BAD_MSG);
      else {
        byte[] mb = new byte[cb.length - Sodium.crypto_box_macbytes()];
        int result = Sodium.crypto_secretbox_open_easy(mb, cb, cb.length, nb, kb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(mb,Base64.NO_WRAP));
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  // ***************************************************************************
  // * Secret-key cryptography - authentication
  // ***************************************************************************
  @ReactMethod
  public void crypto_auth(String in, String k, final Promise p){
    try {
      byte[] out = new byte[Sodium.crypto_auth_bytes()];
      byte[] inb = Base64.decode(in, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_auth_keybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else {
        int result = Sodium.crypto_auth(out, inb, inb.length, kb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(out,Base64.NO_WRAP));
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void crypto_auth_verify(String h, String in, String k, final Promise p){
    try {
      byte[] inb = Base64.decode(in, Base64.NO_WRAP);
      byte[] hb = Base64.decode(h, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_auth_keybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (hb.length != Sodium.crypto_auth_bytes())
        p.reject(ESODIUM,ERR_BAD_MAC);
      else {
        int result = Sodium.crypto_auth_verify(hb, inb, inb.length, kb);
        p.resolve(result);
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  // ***************************************************************************
  // * Public-key cryptography - authenticated encryption
  // ***************************************************************************
  @ReactMethod
  public void crypto_box_keypair(final Promise p){
    try {
      byte[] pk = new byte[Sodium.crypto_box_publickeybytes()];
      byte[] sk = new byte[Sodium.crypto_box_secretkeybytes()];

      if (Sodium.crypto_box_keypair(pk, sk) != 0)
        p.reject(ESODIUM,ERR_FAILURE);
      else {
        WritableNativeMap result = new WritableNativeMap();
        result.putString("pk",Base64.encodeToString(pk,Base64.NO_WRAP));
        result.putString("sk",Base64.encodeToString(sk,Base64.NO_WRAP));
        p.resolve(result);
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void crypto_box_easy(final String m, final String n, final String pk, final String sk, final Promise p) {
    try {
      byte[] mb = Base64.decode(m, Base64.NO_WRAP);
      byte[] nb = Base64.decode(n, Base64.NO_WRAP);
      byte[] pkb = Base64.decode(pk, Base64.NO_WRAP);
      byte[] skb = Base64.decode(sk, Base64.NO_WRAP);
      if (pkb.length != Sodium.crypto_box_publickeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (skb.length != Sodium.crypto_box_secretkeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (nb.length != Sodium.crypto_box_noncebytes())
        p.reject(ESODIUM,ERR_BAD_NONCE);
      else {
        byte[] cb = new byte[mb.length + Sodium.crypto_box_macbytes()];
        int result = Sodium.crypto_box_easy(cb, mb, mb.length, nb, pkb, skb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(cb,Base64.NO_WRAP));
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void crypto_box_easy_afternm(final String m, final String n, final String k, final Promise p) {
    try {
      byte[] mb = Base64.decode(m, Base64.NO_WRAP);
      byte[] nb = Base64.decode(n, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_box_secretkeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (nb.length != Sodium.crypto_box_noncebytes())
        p.reject(ESODIUM,ERR_BAD_NONCE);
      else {
        byte[] cb = new byte[mb.length + Sodium.crypto_box_macbytes()];
        int result = Sodium.crypto_box_easy_afternm(cb, mb, mb.length, nb, kb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(cb,Base64.NO_WRAP));
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void crypto_box_open_easy(final String c, final String n, final String pk, final String sk, final Promise p) {
    try {
      byte[] cb = Base64.decode(c, Base64.NO_WRAP);
      byte[] nb = Base64.decode(n, Base64.NO_WRAP);
      byte[] pkb = Base64.decode(pk, Base64.NO_WRAP);
      byte[] skb = Base64.decode(sk, Base64.NO_WRAP);
      if (pkb.length != Sodium.crypto_box_publickeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (skb.length != Sodium.crypto_box_secretkeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (nb.length != Sodium.crypto_box_noncebytes())
        p.reject(ESODIUM,ERR_BAD_NONCE);
      else if (cb.length < Sodium.crypto_box_macbytes())
        p.reject(ESODIUM,ERR_BAD_MSG);
      else {
        byte[] mb = new byte[cb.length - Sodium.crypto_box_macbytes()];
        int result = Sodium.crypto_box_open_easy(mb, cb, cb.length, nb, pkb, skb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(mb,Base64.NO_WRAP));
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void crypto_box_open_easy_afternm(final String c, final String n, final String k, final Promise p) {
    try {
      byte[] cb = Base64.decode(c, Base64.NO_WRAP);
      byte[] nb = Base64.decode(n, Base64.NO_WRAP);
      byte[] kb = Base64.decode(k, Base64.NO_WRAP);
      if (kb.length != Sodium.crypto_box_secretkeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (nb.length != Sodium.crypto_box_noncebytes())
        p.reject(ESODIUM,ERR_BAD_NONCE);
      else if (cb.length < Sodium.crypto_box_macbytes())
        p.reject(ESODIUM,ERR_BAD_MSG);
      else {
        byte[] mb = new byte[cb.length - Sodium.crypto_box_macbytes()];
        int result = Sodium.crypto_box_open_easy_afternm(mb, cb, cb.length, nb, kb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(mb,Base64.NO_WRAP));
      }
    }
    catch(Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  @ReactMethod
  public void crypto_box_beforenm(final String pk, final String sk, final Promise p) {
    try {
      byte[] pkb = Base64.decode(pk, Base64.NO_WRAP);
      byte[] skb = Base64.decode(sk, Base64.NO_WRAP);
      if (pkb.length != Sodium.crypto_box_publickeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else if (skb.length != Sodium.crypto_box_secretkeybytes())
        p.reject(ESODIUM,ERR_BAD_KEY);
      else {
        byte[] s = new byte[Sodium.crypto_box_secretkeybytes()];
        int result = Sodium.crypto_box_beforenm(s, pkb, skb);
        if (result != 0)
          p.reject(ESODIUM,ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(s,Base64.NO_WRAP));
      }
    }
    catch (Throwable t) {
      p.reject(ESODIUM,ERR_FAILURE,t);
    }
  }

  // ***************************************************************************
  // * Public-key cryptography - signatures
  // ***************************************************************************
  @ReactMethod
  public void crypto_sign_detached(final String msg, final String sk, final Promise p) {
    try {
      byte[] msgb = Base64.decode(msg, Base64.NO_WRAP);
      byte[] skb  = Base64.decode(sk, Base64.NO_WRAP);
      if (skb.length != Sodium.crypto_sign_secretkeybytes()){
        p.reject(ESODIUM,ERR_BAD_KEY);
      }
      else {
        byte[] sig = new byte[Sodium.crypto_sign_bytes()];
        int result = Sodium.crypto_sign_detached(sig, msgb, msgb.length, skb);
        if (result != 0)
          p.reject(ESODIUM, ERR_FAILURE);
        else {
          p.resolve(Base64.encodeToString(sig, Base64.NO_WRAP));
        }
      }
    }
    catch(Throwable t) {
      p.reject(ESODIUM, ERR_FAILURE, t);
    }
  }

  @ReactMethod
  public void crypto_sign_verify_detached(final String sig, final String msg, final String pk, final Promise p) {
    try {
      byte[] sigb = Base64.decode(sig, Base64.NO_WRAP);
      byte[] msgb = Base64.decode(msg, Base64.NO_WRAP);
      byte[] pkb  = Base64.decode(pk, Base64.NO_WRAP);
      if (pkb.length != Sodium.crypto_sign_publickeybytes()){
        p.reject(ESODIUM,ERR_BAD_KEY);
      }
      if (sigb.length != Sodium.crypto_sign_bytes()){
        p.reject(ESODIUM,ERR_BAD_SIG);
      }
      else {
        int result = Sodium.crypto_sign_verify_detached(sigb, msgb, msgb.length, pkb);
        if (result != 0)
          p.reject(ESODIUM, ERR_FAILURE);
        else
          p.resolve(true);
      }
    }
    catch(Throwable t) {
      p.reject(ESODIUM, ERR_FAILURE, t);
    }
  }

  @ReactMethod
  public void crypto_sign_seed_keypair(final String seed, final Promise p) {
    try {
      byte[] seedb = Base64.decode(seed, Base64.NO_WRAP);
      byte[] pk = new byte[Sodium.crypto_sign_publickeybytes()];
      byte[] sk = new byte[Sodium.crypto_sign_secretkeybytes()];

      if (seedb.length != Sodium.crypto_sign_seedbytes()) {
        p.reject(ESODIUM,ERR_BAD_SEED);
      }
      else {
        int result = Sodium.crypto_sign_seed_keypair(pk, sk, seedb);
        if (result != 0)
          p.reject(ESODIUM, ERR_FAILURE);
        else {
          WritableNativeMap map = new WritableNativeMap();
          map.putString("pk",Base64.encodeToString(pk,Base64.NO_WRAP));
          map.putString("sk",Base64.encodeToString(sk,Base64.NO_WRAP));
          p.resolve(map);
        }
      }
    }
    catch(Throwable t) {
      p.reject(ESODIUM, ERR_FAILURE, t);
    }
  }

  @ReactMethod
  public void crypto_sign_ed25519_sk_to_seed(final String sk, final Promise p) {
    try {
      byte[] skb = Base64.decode(sk, Base64.NO_WRAP);
      if(skb.length != Sodium.crypto_sign_secretkeybytes()){
        p.reject(ESODIUM,ERR_BAD_KEY);
      }
      else {
        byte[] seed = new byte[Sodium.crypto_box_secretkeybytes()];
        int result = Sodium.crypto_sign_ed25519_sk_to_seed(seed, skb);
        if (result != 0)
          p.reject(ESODIUM, ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(seed, Base64.NO_WRAP));
      }
    }
    catch(Throwable t) {
      p.reject(ESODIUM, ERR_FAILURE, t);
    }
  }

  @ReactMethod
  public void crypto_sign_ed25519_pk_to_curve25519(final String pk, final Promise p) {
    try {
      byte[] pkb = Base64.decode(pk, Base64.NO_WRAP);
      if(pkb.length != Sodium.crypto_sign_publickeybytes()){
        p.reject(ESODIUM,ERR_BAD_KEY);
      }
      else {
        byte[] curve_pk = new byte[Sodium.crypto_sign_publickeybytes()];
        int result = Sodium.crypto_sign_ed25519_pk_to_curve25519(curve_pk, pkb);
        if (result != 0)
          p.reject(ESODIUM, ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(curve_pk, Base64.NO_WRAP));
      }
    }
    catch(Throwable t) {
      p.reject(ESODIUM, ERR_FAILURE, t);
    }
  }

  @ReactMethod
  public void crypto_sign_ed25519_sk_to_curve25519(final String sk, final Promise p) {
    try {
      byte[] skb = Base64.decode(sk, Base64.NO_WRAP);
      if(skb.length != Sodium.crypto_sign_secretkeybytes()){
        p.reject(ESODIUM,ERR_BAD_KEY);
      }
      else {
        byte[] curve_sk = new byte[Sodium.crypto_box_secretkeybytes()];
        int result = Sodium.crypto_sign_ed25519_sk_to_curve25519(curve_sk, skb);
        if (result != 0)
          p.reject(ESODIUM, ERR_FAILURE);
        else
          p.resolve(Base64.encodeToString(curve_sk, Base64.NO_WRAP));
      }
    }
    catch(Throwable t) {
      p.reject(ESODIUM, ERR_FAILURE, t);
    }
  }
}
