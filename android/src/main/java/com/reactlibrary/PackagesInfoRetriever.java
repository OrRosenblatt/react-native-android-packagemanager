package com.reactlibrary;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackagesInfoRetriever {

    private final ReactApplicationContext reactContext;

    public PackagesInfoRetriever(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public void getPackageInfo(String fullPath, Promise promise) {
        try {
            PackageManager pm = this.reactContext.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(fullPath, 0);

            PackageInfoMapping info = new PackageInfoMapping.Builder(pi, pm).withLabel(true).build();
            WritableMap map = info.asWritableMap();

            promise.resolve(map);
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.reject(null, ex.getMessage());
        }
    }

    public void getInstalledPackages(ReadableMap options, Promise promise) {
        try {
            WritableArray array = Arguments.createArray();

            boolean loadLabel = options != null && options.hasKey("loadLabel") ? options.getBoolean("loadLabel")
                    : false;

            PackageManager pm = this.reactContext.getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            for (PackageInfo pi : packages) {
                PackageInfoMapping info = new PackageInfoMapping.Builder(pi, pm).withLabel(loadLabel).build();
                WritableMap map = info.asWritableMap();

                array.pushMap(map);
            }

            promise.resolve(array);
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.reject(null, ex.getMessage());
        }
    }
}
