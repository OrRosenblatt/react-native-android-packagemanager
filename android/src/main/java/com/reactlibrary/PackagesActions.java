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

public class PackagesActions {

  private final int UNINSTALL_REQUEST_CODE = 1;
  private final String USER_DECLINED_KEY = "USER_DECLINED";
  private final int USER_DECLINED_CODE = Activity.RESULT_CANCELED;
  private final String UNINSTALL_FAILED_KEY = "UNINSTALL_FAILED";
  private final int UNINSTALL_FAILED_CODE = Activity.RESULT_FIRST_USER;
  private final String APP_NOT_FOUND_KEY = "APP_NOT_FOUND";

  private ReactApplicationContext reactContext;
  private PackagesInfoRetriever packagesInfoRetriever;
  private Promise uninstallPromise;
  private final ActivityEventListener activityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      super.onActivityResult(requestCode, resultCode, intent);
      if ((requestCode == UNINSTALL_REQUEST_CODE) && (uninstallPromise != null)) {
        if (resultCode == Activity.RESULT_OK) {
          uninstallPromise.resolve(true);
        } else if (resultCode == USER_DECLINED_CODE) {
          uninstallPromise.reject(USER_DECLINED_KEY, USER_DECLINED_KEY);
        } else if (resultCode == UNINSTALL_FAILED_CODE) {
          uninstallPromise.reject(UNINSTALL_FAILED_KEY, UNINSTALL_FAILED_KEY);
        }
        uninstallPromise = null;
      }
    }
  };

  public PackagesActions(ReactApplicationContext reactContext) {
    this.reactContext = reactContext;
    this.packagesInfoRetriever = new PackagesInfoRetriever(this.reactContext);
    this.reactContext.addActivityEventListener(this.activityEventListener);
  }

  public void uninstallPackage(String path, Promise promise) {
    if (!this.isAppInstalled(path)) {
      promise.reject(APP_NOT_FOUND_KEY, APP_NOT_FOUND_KEY);
      return;
    }

    try {
      this.uninstallPromise = promise;
      Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
      intent.setData(Uri.parse("package:" + path));
      intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
      this.reactContext.getCurrentActivity().startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
    } catch (Exception ex) {
      ex.printStackTrace();
      promise.reject(UNINSTALL_FAILED_KEY, ex.getMessage());
      this.uninstallPromise = null;
    }
  }

  private boolean isAppInstalled(String path) {
    try {
      PackageManager pm = this.reactContext.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(path, PackageManager.GET_ACTIVITIES);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
}
