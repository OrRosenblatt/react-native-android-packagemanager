package com.reactlibrary;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import 	android.util.Base64;
import 	android.content.pm.PackageManager.NameNotFoundException;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import java.io.ByteArrayOutputStream;


public class PackageInfoMapping {

  private String packageName;
  private String label;
  private String versionName;
  private int versionCode;
  private long firstInstallTime;
  private long lastUpdateTime;
  private boolean isSystemApp;
  private String appIconBase64;

  private PackageInfoMapping(PackageInfo packageInfo, ApplicationInfo applicationInfo, String label,
      String appIconBase64) {
    this.label = label;
    this.appIconBase64 = appIconBase64;
    this.packageName = packageInfo.packageName;
    this.versionName = packageInfo.versionName;
    this.versionCode = packageInfo.versionCode;
    this.firstInstallTime = packageInfo.firstInstallTime;
    this.lastUpdateTime = packageInfo.lastUpdateTime;
    this.isSystemApp = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
  }

  public WritableMap asWritableMap() {
    WritableMap map = Arguments.createMap();

    map.putString("label", this.label);
    map.putString("package", this.packageName);
    map.putString("versionName", this.versionName);
    map.putInt("versionCode", this.versionCode);
    map.putDouble("firstInstallTime", this.firstInstallTime);
    map.putDouble("lastUpdateTime", this.lastUpdateTime);
    map.putBoolean("isSystemApp", this.isSystemApp);
    if (this.appIconBase64 != null) {
      map.putString("appIconBase64", this.appIconBase64);
    }

    return map;
  }

  public static class Builder {

    private boolean loadLabel;
    private PackageInfo packageInfo;
    private PackageManager packageManager;
    private ApplicationInfo applicationInfo;
    private String appIconBase64;
    private boolean includeAppIcon;
    private int appIconSizeInPixel;
  

    public Builder(PackageInfo packageInfo, PackageManager packageManager) {
      this.packageInfo = packageInfo;
      this.packageManager = packageManager;
      this.applicationInfo = packageInfo.applicationInfo;
    }

    public Builder withLabel(boolean loadLabel) {
      this.loadLabel = loadLabel;
      return this;
    }

    public Builder includeAppIcon(boolean includeAppIcon, int appIconSizeInPixel) {
      this.includeAppIcon = includeAppIcon;
      this.appIconSizeInPixel = appIconSizeInPixel;

      return this;
    }

    public PackageInfoMapping build() {
      String label = this.loadLabel ? this.loadPackageLabel() : null;
      String appIconBase64Encode = this.includeAppIcon
          ? this.getAppIconAsBase64()
          : null;

      return new PackageInfoMapping(this.packageInfo, this.applicationInfo, label, appIconBase64Encode);
    }

    private String loadPackageLabel() {
      String label;
      try {
        label = this.applicationInfo.loadLabel(this.packageManager).toString();
      } catch (Exception exc) {
        label = this.applicationInfo.packageName;
      }
      return label;
    }

    private String getAppIconAsBase64() {
      String packageName = this.applicationInfo.packageName;
      if (packageName.isEmpty())
        return new String("");

      String base64Encoded = "";
      Bitmap bitmap, smallBitmap;

      try {
        Drawable appIcon = this.packageManager.getApplicationIcon(this.applicationInfo);
        if (appIcon == null)
          return "";

        if (appIcon instanceof BitmapDrawable) {
          bitmap = ((BitmapDrawable) appIcon).getBitmap();
        } else {
          bitmap = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(),
              Bitmap.Config.ARGB_8888);
        }
        smallBitmap = Bitmap.createScaledBitmap(bitmap, this.appIconSizeInPixel, this.appIconSizeInPixel, false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (smallBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream)) {
          byte[] byteArray = byteArrayOutputStream.toByteArray();
          base64Encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

      } catch (Exception ex) {
        ex.printStackTrace();
      }

      return base64Encoded;
    }
  }
}
