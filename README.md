# react-native-android-packagemanager

Gives you some access to Android's [PackageManager](https://developer.android.com/reference/android/content/pm/PackageManager.html) APIs, for instance to read the metadata inside an APK file.

## Getting started

`$ npm install react-native-android-packagemanager --save`

### Easy installation

`$ react-native link react-native-android-packagemanager`

### Manual installation

#### Android

1.  Open up `android/app/src/main/java/[...]/MainActivity.java`

-   Add `import com.reactlibrary.RNAndroidPackagemanagerPackage;` to the imports at the top of the file
-   Add `new RNAndroidPackagemanagerPackage()` to the list returned by the `getPackages()` method

2.  Append the following lines to `android/settings.gradle`:
    ```
    include ':react-native-android-packagemanager'
    project(':react-native-android-packagemanager').projectDir = new File(rootProject.projectDir,   '../node_modules/react-native-android-packagemanager/android')
    ```
3.  Insert the following lines inside the dependencies block in `android/app/build.gradle`:
    ```
      compile project(':react-native-android-packagemanager')
    ```

## Usage

```javascript
import RNAndroidPM, { UNINSTALL_ERRORS } from 'react-native-android-packagemanager';

RNAndroidPM.getPackageInfo('/storage/emulated/0/myapp.apk').then(info => {
    console.log(info);
    /*
      {
        package: "com.example.myapp",
        label: "My App",
        versionName: "1.2.3",
        versionCode: 3,
        firstInstallTime: 1185920,
        lastUpdateTime: 1283058,
        isSystemApp: false
      }
    */
});

RNAndroidPM.getInstalledPackages({}).then(packages => {
    console.log(packages);
    /*
      [
        {
          package: "com.example.myapp",
          label: "My App",
          versionName: "1.2.3",
          versionCode: 3,
          firstInstallTime: 1185920,
          lastUpdateTime: 1283058,
          isSystemApp: false
        },
        {
          package: "com.example.anotherapp",
          label: "Another App",
          versionName: "1.0.0",
          versionCode: 1,
          firstInstallTime: 1185920,
          lastUpdateTime: 1283058,
          isSystemApp: false
        }
      ]
    */
});

RNAndroidPM.uninstallPackage('com.developer.app')
    .then(() => console.log('uninstall finished successfully'))
    .catch(err => {
        switch (err.code) {
            case UNINSTALL_ERRORS.APP_NOT_FOUND:
            case UNINSTALL_ERRORS.USER_DECLINED:
                //...
                break;
            case UNINSTALL_ERRORS.UNINSTALL_FAILED:
                //...
                break;
            default:
                console.log('unrecognized error');
        }
    });
```
### PackageVersion 1.5.0 Icons Support
#### getInstalledPackages
As from Version 1.5.0 getInstalledPackages gets additional params inside options dictionary:
##### loadLabel
boolean. Indicates if we want to get the app's label.
##### shouldGetAppsIcons
boolean. Indicates if we want to get apps icons as base64 encoding string of PNG format.
##### iconPixelSquareSize
int. reize the icon to square size iconPixelSquareSize X iconPixelSquareSize pixels. Default size 45X45 pixels

```javascript
import RNAndroidPM, { UNINSTALL_ERRORS } from 'react-native-android-packagemanager';

RNAndroidPM.getInstalledPackages({loadLabel: true, shouldGetAppsIcons: true, iconPixelSquareSize: 45}).then(info => {
    console.log(info);
    /*
      {
        package: "com.example.myapp",
        label: "My App",
        versionName: "1.2.3",
        versionCode: 3,
        firstInstallTime: 1185920,
        lastUpdateTime: 1283058,
        isSystemApp: false,        
        appIconBase64:"iVBORw0KGgoAAAANSUhEUgAAAC0AAAAtCAYAAAA6GuKaAAAABHNCSVQICAgIfAhkiAAABsRJREFU↵WIXVmVtsHFcZx39nbrvr3bV3nTaXxqRpA6RUpaWlNS0BkSCK1BTS4KC2SVFDWgSVEEggUYEQFKkg↵VIkHqiKhPiA5BIe02K0at31AoDyAgkIrUIHSFuWi2I7jJL7tetf2zu3wMDt78c7sjB0nUv8vPnP2↵u/znO98533fG8D6EWDVLrxT70ZP70XQwKyBd0A1QVSiX/otmfJkvJN5ZDVeXR3po9kY6u04xNxtP↵XlEA5Tl2Z799OW5XRnq42I/l7G+Zl4AjQRWeZVt6f9UAN5kcfF6syP/ylY6WDmFbX2macyUv3p1G↵CLDcZnFFQFoVDJ4zOXjaBK3BparB9GSeAzfEXKqVkD5aOoNtba49O5LhT2WYs2VsE8+erHBixqlP↵SAl7upfFI77wy7PHkHK7//j49QafXavFNuACSnWc0QS7/lqqp002B/fGT5V4gkcm78JQ/+4//uaO↵DlJqXBdeijy00eB02eXEjA1ASoUv/aUMepVCsgN2JmLxiRb6iVS4dcbxRX/8kSQ3ZZQIJQ8VF766↵yWiZf/2CRcGSSOCxN+ep+NklOcme/Iei7EZ7/zQ1wp2qiE04oYhAwgA71+lc36EggIHejvoPQn4w↵ju1oBoXqxpZw6K6O9rKAJWFvj0HfdXpbuU92awgBpgtabVbA0PR4lI/2pIfKG/zh7XmVshN+SnRo↵gr09Bo9+IDi6QXh4o4Er4aV70vVJTd8QrlEVaf9z5W0/NZ6+OUnR8khbLmgKbEgo3JFX6dJWXljv↵zGv8Y9b2CpEmwLHhxelNPNg9EqYTHuk/SgnkAXBkjfDahODRTQb7egx2XKtdFmGArdU98tRHU/XJ↵hH6WwZndyyP9JykpzUL16Oy9pr4g269pn6srgSPh47mGM9SxQZEvh8kHp0exuap+pkr67LxL+mj7↵ivvE5gS/vK0etcfenOfwObOtzjO3pMhowutdagsnYGjmW+zJPxePtBBeea1ijSHIaoIf/GeRC/d3↵tSWwfrjA9z6cYGNK4fi0w8CoyejO9jrrXivw+rZ06w+qPRUkH4v0nCNxAWzJ2oiitX29Rs+rBa9u↵q/D1LYlIHSyJHpSolj4ZJB6c06K5Rjt+5xZzzx25J4N8KM/TjZsrApoQrfalOB0oG2hB2hPAegCE↵wie6q42RKnir4IRyFwKOTVg8/zGvCG3r1rj37RLf3JLw8jVEB10wurCkp01l4D79ZKBOiH94ZWE/↵WN/lgc7bDo963cFERfLTdxdJBzX1VTy+yeCpm5O15++8tcDQeStUHuCHNyV45n8VzpRrS3qOvlxP↵mHysBR8uSzk37Z0AroRHllH1olCyJa9dsHj4b/PgZ2Vfvi2vWN1Pccr8UU2hjblFRzI4btE/YjJr↵SQ6OmAyMma1L34DBcYtTJbdOOJuL5BO7nL06L2Vxyou2BPb1NEf72KTFxKJsaUdfGreouJJFFw4s↵6fp+O2KiKfDIG/P1yQJ5DuTbFoN4fSZw6QIpfzMJoH+kuWBMmRJHtvbPfdfp3l13ib2jExa6AsPn↵7fpkZ44owssifeAGsSgU3vCfE4oXKR+u9HqeIKxbck7/fsykbEuKtuTIWMPLfy7elSs2aYC9PYne↵dK4eSV3xCByftlFFuLHZ6i0F4NCoR3J80eWJfy7UhWzra3F5LKtFG7xkW+aiE9rOSrweeelmPThi↵YlTfSBXws3cr/KtYv5FvTik8u62TBzrjRTo26cZjD2BrRuW9ktMiJ4H35hwGRq2a8aQCP78lxZP/↵XuCdUvNJcmunyve3JgDoyBrszkUTj0X6Dxct06q4tZ5013qddLWPfuGciduQy786XeH4VOvLtEDC↵iR1Zbu9SGRyvByPdZURGPDKnD52p7G4kbEtqhMH7NLC3x2CNITBdOH7RDrTTiM1JBdmXozevoivN↵Z3+5YPK7s5Un2+lHRvqF87Z0bC9ypgv7Q27YNYNHZiCkq+vUBIUvBrep/SMmCT+EQmFfjx7KLTLS↵PmGIJgxgPpj32tKlqMhQwrDkfJcuz5+SocJtI91YBXvzGlvSyzohl42LFcmfL3nNVSZnsCsbnNtt↵WfiEXbjihMG7NPsFqjQbfkULZTIwtrDDH4dn1+oj1+Ds8Jj5jSCZ8PA5/Nof3pmL+Dyyirg7X/cl↵HfcXQTKhpLNrklv98Y1XITV8XNtw8mTXJDJBMqFsKtVuMbwTvnLwv77NF4O9h5J2LLvJwNWE71O6↵wZX16q37KiJ0h6m6hlsx0QUMjLb/QrTa8DtCoQT/uyGUdDINVsUbt7sXXkl0ZIMTIZT05EVS+byx↵YF3dINegajA1WY78Vv2+wf8Bw7xOpRNIL6sAAAAASUVORK5CYII=↵"
      }
    */
```
