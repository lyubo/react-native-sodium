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
            byte[] publicKey = new byte[Sodium.PUBLICKEY_BYTES];
            byte[] secretKey = new byte[Sodium.SECRETKEY_BYTES];

            Sodium.crypto_box_keypair(publicKey, secretKey);

            WritableNativeMap result = new WritableNativeMap();
            result.putString("secretKey",Base64.encodeToString(secretKey,Base64.DEFAULT));
            result.putString("publicKey",Base64.encodeToString(publicKey,Base64.DEFAULT));
            p.resolve(result);
        }
        catch (Throwable t) {
            p.reject(t);
        }
    }

}
