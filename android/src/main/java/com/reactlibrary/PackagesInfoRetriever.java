package com.reactlibrary;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.Intent;
import android.net.Uri;
import 	android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import 	android.util.Base64;

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
import java.io.ByteArrayOutputStream;

public class PackagesInfoRetriever {

    private final ReactApplicationContext reactContext;

    public PackagesInfoRetriever(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    public void getPackageInfo(String fullPath, Promise promise) {
        try {
            PackageManager pm = this.reactContext.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(fullPath, 0);
            String appIconBase64Encode = getBitmapOfAnAppAsBase64(pm, pi.packageName);   

            PackageInfoMapping info = new PackageInfoMapping.Builder(pi, pm, appIconBase64Encode).withLabel(true).build();
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
                String appIconBase64Encode = getBitmapOfAnAppAsBase64(pm, pi.packageName);                
                PackageInfoMapping info = new PackageInfoMapping.Builder(pi, pm, appIconBase64Encode).withLabel(loadLabel).build();
                WritableMap map = info.asWritableMap();

                array.pushMap(map);
            }

            promise.resolve(array);
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.reject(null, ex.getMessage());
        }
    }

    private String getBitmapOfAnAppAsBase64(PackageManager packageManager, String packageName) {
        if(packageName.isEmpty() ) return new String("");

        String base64Encoded = "";
        Bitmap bitmap, smallBitmap;

        try {
            Drawable appIcon = getAppDrawableIcon(packageManager, packageName);
            if (appIcon == null) return "";

            if(appIcon instanceof BitmapDrawable) {
                bitmap= ((BitmapDrawable)appIcon).getBitmap();
            } else {
                bitmap = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            smallBitmap = Bitmap.createScaledBitmap(bitmap, 45, 45, false);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (smallBitmap.compress(Bitmap.CompressFormat.WEBP, 90, byteArrayOutputStream)) {
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                base64Encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        
                

        } catch(Exception ex) {
              ex.printStackTrace();
        }
     

        return  base64Encoded;
    }

    private Drawable getAppDrawableIcon(PackageManager packageManager, String pckageName) {
         try {
            ApplicationInfo app = packageManager.getApplicationInfo(pckageName, 0);        

            Drawable icon = packageManager.getApplicationIcon(app);

            return icon;
        } catch (NameNotFoundException e) {
           return null;
        }
    }
}
