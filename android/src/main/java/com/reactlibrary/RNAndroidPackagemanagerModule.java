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

public class RNAndroidPackagemanagerModule extends ReactContextBaseJavaModule {

  private final PackagesInfoRetriever packagesInfoRetriever;
  private final PackagesActions packagesActions;

  public RNAndroidPackagemanagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.packagesInfoRetriever = new PackagesInfoRetriever(reactContext);
    this.packagesActions = new PackagesActions(reactContext);
  }

  @Override
  public String getName() {
    return "RNAndroidPackagemanager";
  }

  @ReactMethod
  public void getPackageInfo(String path, Promise promise) {
    this.packagesInfoRetriever.getPackageInfo(path, promise);
  }

  @ReactMethod
  public void getInstalledPackages(ReadableMap options, Promise promise) {
    this.packagesInfoRetriever.getInstalledPackages(options, promise);
  }

  @ReactMethod
  public void uninstallPackage(String path, Promise promise) {
    this.packagesActions.uninstallPackage(path, promise);
  }
}
