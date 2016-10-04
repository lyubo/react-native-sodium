package org.libsodium.rn;

/**
 * Created by lyubo on 21/09/16.
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


    @ReactMethod
    public void sodium_version_string(final Promise p){
        try {
            p.resolve(Sodium.sodium_version_string());
        }
        catch (Throwable t) {
            p.reject(t);
        }
    }

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
