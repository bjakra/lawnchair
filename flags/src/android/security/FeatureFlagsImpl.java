package android.security;

import android.os.Build;
import android.os.flagging.AconfigPackage;
import android.util.Log;
/** @hide */
final class FeatureFlagsImpl implements FeatureFlags {
    private static final String TAG = "FeatureFlagsImplExport";
    private static volatile boolean isCached = false;

    private static boolean aapmApi = false;
    private static boolean aapmApiV2 = false;
    private static boolean aapmFeatureDisableInsecureWifiAutojoin = false;
    private static boolean aflApi = false;
    private static boolean appLockApis = false;
    private static boolean asmRestrictionsEnabled = false;
    private static boolean autoSimPinManagement = false;
    private static boolean certificateTransparencyConfiguration = false;
    private static boolean conscryptNetworkSecurityConfig = false;
    private static boolean contentUriPermissionApis = false;
    private static boolean deprecateUsesCleartextTraffic2 = false;
    private static boolean dynamicInstrumentationApi = false;
    private static boolean enableDeviceCertificates = false;
    private static boolean enableIntentMatchingFlags = false;
    private static boolean enforceIntentFilterMatch = false;
    private static boolean extendAapmToA11yServices = false;
    private static boolean frpEnforcement = false;
    private static boolean fsverityApi = false;
    private static boolean keyinfoUnlockedDeviceRequired = false;
    private static boolean keystoreGrantApi = false;
    private static boolean mgf1DigestSetterV2 = false;
    private static boolean preventIntentRedirect = false;
    private static boolean secureLockDevice = false;
    private static boolean secureLockdown = false;
    private static boolean strictModeViolationForImplicitUriGrantsEnabled = false;
    private static boolean talismanServiceApi = false;
    private static boolean uprobestatsBridgeService = false;
    private void init() {
        try {
            AconfigPackage reader = AconfigPackage.load("android.security");
            extendAapmToA11yServices = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("extend_aapm_to_a11y_services", false);
            secureLockDevice = Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : reader.getBooleanFlagValue("secure_lock_device", false);
            secureLockdown = Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : reader.getBooleanFlagValue("secure_lockdown", false);
            talismanServiceApi = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("talisman_service_api", false);
            enableDeviceCertificates = reader.getBooleanFlagValue("enable_device_certificates", false);
            aflApi = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("afl_api", false);
            frpEnforcement = Build.VERSION.SDK_INT >= 35 ? true : reader.getBooleanFlagValue("frp_enforcement", false);
            fsverityApi = Build.VERSION.SDK_INT >= 35 ? true : reader.getBooleanFlagValue("fsverity_api", false);
            keyinfoUnlockedDeviceRequired = Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : reader.getBooleanFlagValue("keyinfo_unlocked_device_required", false);
            keystoreGrantApi = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("keystore_grant_api", false);
            mgf1DigestSetterV2 = Build.VERSION.SDK_INT >= 35 ? true : reader.getBooleanFlagValue("mgf1_digest_setter_v2", false);
            certificateTransparencyConfiguration = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("certificate_transparency_configuration", false);
            conscryptNetworkSecurityConfig = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("conscrypt_network_security_config", false);
            deprecateUsesCleartextTraffic2 = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("deprecate_uses_cleartext_traffic2", false);
            enableIntentMatchingFlags = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_intent_matching_flags", false);
            strictModeViolationForImplicitUriGrantsEnabled = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("strict_mode_violation_for_implicit_uri_grants_enabled", false);
            aapmApi = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("aapm_api", false);
            aapmApiV2 = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("aapm_api_v2", false);
            aapmFeatureDisableInsecureWifiAutojoin = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("aapm_feature_disable_insecure_wifi_autojoin", false);
            asmRestrictionsEnabled = Build.VERSION.SDK_INT >= 35 ? true : reader.getBooleanFlagValue("asm_restrictions_enabled", false);
            contentUriPermissionApis = Build.VERSION.SDK_INT >= 35 ? true : reader.getBooleanFlagValue("content_uri_permission_apis", false);
            dynamicInstrumentationApi = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("dynamic_instrumentation_api", false);
            enforceIntentFilterMatch = Build.VERSION.SDK_INT >= 35 ? true : reader.getBooleanFlagValue("enforce_intent_filter_match", false);
            preventIntentRedirect = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("prevent_intent_redirect", false);
            uprobestatsBridgeService = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("uprobestats_bridge_service", false);
            appLockApis = reader.getBooleanFlagValue("app_lock_apis", false);
            autoSimPinManagement = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("auto_sim_pin_management", false);
        } catch (Exception e) {
            // pass
            Log.e(TAG, e.toString());
        } catch (LinkageError e) {
            // for mainline module running on older devices.
            // This should be replaces to version check, after the version bump.
            Log.w(TAG, e.toString());
        }
        isCached = true;
    }

    @Override
    public boolean aapmApi() {
        if (!isCached) {
            init();
        }
        return aapmApi;
    }

    @Override
    public boolean aapmApiV2() {
        if (!isCached) {
            init();
        }
        return aapmApiV2;
    }

    @Override
    public boolean aapmFeatureDisableInsecureWifiAutojoin() {
        if (!isCached) {
            init();
        }
        return aapmFeatureDisableInsecureWifiAutojoin;
    }

    @Override
    public boolean aflApi() {
        if (!isCached) {
            init();
        }
        return aflApi;
    }

    @Override
    public boolean appLockApis() {
        if (!isCached) {
            init();
        }
        return appLockApis;
    }

    @Override
    public boolean asmRestrictionsEnabled() {
        if (!isCached) {
            init();
        }
        return asmRestrictionsEnabled;
    }

    @Override
    public boolean autoSimPinManagement() {
        if (!isCached) {
            init();
        }
        return autoSimPinManagement;
    }

    @Override
    public boolean certificateTransparencyConfiguration() {
        if (!isCached) {
            init();
        }
        return certificateTransparencyConfiguration;
    }

    @Override
    public boolean conscryptNetworkSecurityConfig() {
        if (!isCached) {
            init();
        }
        return conscryptNetworkSecurityConfig;
    }

    @Override
    public boolean contentUriPermissionApis() {
        if (!isCached) {
            init();
        }
        return contentUriPermissionApis;
    }

    @Override
    public boolean deprecateUsesCleartextTraffic2() {
        if (!isCached) {
            init();
        }
        return deprecateUsesCleartextTraffic2;
    }

    @Override
    public boolean dynamicInstrumentationApi() {
        if (!isCached) {
            init();
        }
        return dynamicInstrumentationApi;
    }

    @Override
    public boolean enableDeviceCertificates() {
        if (!isCached) {
            init();
        }
        return enableDeviceCertificates;
    }

    @Override
    public boolean enableIntentMatchingFlags() {
        if (!isCached) {
            init();
        }
        return enableIntentMatchingFlags;
    }

    @Override
    public boolean enforceIntentFilterMatch() {
        if (!isCached) {
            init();
        }
        return enforceIntentFilterMatch;
    }

    @Override
    public boolean extendAapmToA11yServices() {
        if (!isCached) {
            init();
        }
        return extendAapmToA11yServices;
    }

    @Override
    public boolean frpEnforcement() {
        if (!isCached) {
            init();
        }
        return frpEnforcement;
    }

    @Override
    public boolean fsverityApi() {
        if (!isCached) {
            init();
        }
        return fsverityApi;
    }

    @Override
    public boolean keyinfoUnlockedDeviceRequired() {
        if (!isCached) {
            init();
        }
        return keyinfoUnlockedDeviceRequired;
    }

    @Override
    public boolean keystoreGrantApi() {
        if (!isCached) {
            init();
        }
        return keystoreGrantApi;
    }

    @Override
    public boolean mgf1DigestSetterV2() {
        if (!isCached) {
            init();
        }
        return mgf1DigestSetterV2;
    }

    @Override
    public boolean preventIntentRedirect() {
        if (!isCached) {
            init();
        }
        return preventIntentRedirect;
    }

    @Override
    public boolean secureLockDevice() {
        if (!isCached) {
            init();
        }
        return secureLockDevice;
    }

    @Override
    public boolean secureLockdown() {
        if (!isCached) {
            init();
        }
        return secureLockdown;
    }

    @Override
    public boolean strictModeViolationForImplicitUriGrantsEnabled() {
        if (!isCached) {
            init();
        }
        return strictModeViolationForImplicitUriGrantsEnabled;
    }

    @Override
    public boolean talismanServiceApi() {
        if (!isCached) {
            init();
        }
        return talismanServiceApi;
    }

    @Override
    public boolean uprobestatsBridgeService() {
        if (!isCached) {
            init();
        }
        return uprobestatsBridgeService;
    }

}
