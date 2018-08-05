export enum UNINSTALL_ERRORS {
    USER_DECLINED = 'USER_DECLINED',
    UNINSTALL_FAILED = 'UNINSTALL_FAILED',
    APP_NOT_FOUND = 'APP_NOT_FOUND'
}

export interface PackageInfo {
    package: string;
    label: string;
    versionName: string;
    versionCode: number;
    firstInstallTime: number;
    lastUpdateTime: number;
    isSystemApp: boolean;
}

declare class RNAndroidPackagemanager {
    static getPackageInfo(fullPath: string): Promise<PackageInfo>;
    static getInstalledPackages(options: {loadLabel: boolean, shouldGetAppsIcons: boolean, iconPixelSquareSize: number}): Promise<PackageInfo[]>;
    static uninstallPackage(packageName: string): Promise<void>;
}

export default RNAndroidPackagemanager;
