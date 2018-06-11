import { NativeModules } from 'react-native';

const { RNAndroidPackagemanager } = NativeModules;

export default RNAndroidPackagemanager;

export const UNINSTALL_ERRORS = {
    USER_DECLINED: 'USER_DECLINED',
    UNINSTALL_FAILED: 'UNINSTALL_FAILED',
    APP_NOT_FOUND: 'APP_NOT_FOUND'
};
