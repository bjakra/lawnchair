package android.security;

import android.os.Build;
/** @hide */
public final class Flags {

    /** @hide */
    public static final String FLAG_AAPM_API = "android.security.aapm_api";
    /** @hide */
    public static final String FLAG_AAPM_API_V2 = "android.security.aapm_api_v2";
    /** @hide */
    public static final String FLAG_AAPM_FEATURE_DISABLE_INSECURE_WIFI_AUTOJOIN = "android.security.aapm_feature_disable_insecure_wifi_autojoin";
    /** @hide */
    public static final String FLAG_AFL_API = "android.security.afl_api";
    /** @hide */
    public static final String FLAG_APP_LOCK_APIS = "android.security.app_lock_apis";
    /** @hide */
    public static final String FLAG_ASM_RESTRICTIONS_ENABLED = "android.security.asm_restrictions_enabled";
    /** @hide */
    public static final String FLAG_AUTO_SIM_PIN_MANAGEMENT = "android.security.auto_sim_pin_management";
    /** @hide */
    public static final String FLAG_CERTIFICATE_TRANSPARENCY_CONFIGURATION = "android.security.certificate_transparency_configuration";
    /** @hide */
    public static final String FLAG_CONSCRYPT_NETWORK_SECURITY_CONFIG = "android.security.conscrypt_network_security_config";
    /** @hide */
    public static final String FLAG_CONTENT_URI_PERMISSION_APIS = "android.security.content_uri_permission_apis";
    /** @hide */
    public static final String FLAG_DEPRECATE_USES_CLEARTEXT_TRAFFIC2 = "android.security.deprecate_uses_cleartext_traffic2";
    /** @hide */
    public static final String FLAG_DYNAMIC_INSTRUMENTATION_API = "android.security.dynamic_instrumentation_api";
    /** @hide */
    public static final String FLAG_ENABLE_DEVICE_CERTIFICATES = "android.security.enable_device_certificates";
    /** @hide */
    public static final String FLAG_ENABLE_INTENT_MATCHING_FLAGS = "android.security.enable_intent_matching_flags";
    /** @hide */
    public static final String FLAG_ENFORCE_INTENT_FILTER_MATCH = "android.security.enforce_intent_filter_match";
    /** @hide */
    public static final String FLAG_EXTEND_AAPM_TO_A11Y_SERVICES = "android.security.extend_aapm_to_a11y_services";
    /** @hide */
    public static final String FLAG_FRP_ENFORCEMENT = "android.security.frp_enforcement";
    /** @hide */
    public static final String FLAG_FSVERITY_API = "android.security.fsverity_api";
    /** @hide */
    public static final String FLAG_KEYINFO_UNLOCKED_DEVICE_REQUIRED = "android.security.keyinfo_unlocked_device_required";
    /** @hide */
    public static final String FLAG_KEYSTORE_GRANT_API = "android.security.keystore_grant_api";
    /** @hide */
    public static final String FLAG_MGF1_DIGEST_SETTER_V2 = "android.security.mgf1_digest_setter_v2";
    /** @hide */
    public static final String FLAG_PREVENT_INTENT_REDIRECT = "android.security.prevent_intent_redirect";
    /** @hide */
    public static final String FLAG_SECURE_LOCK_DEVICE = "android.security.secure_lock_device";
    /** @hide */
    public static final String FLAG_SECURE_LOCKDOWN = "android.security.secure_lockdown";
    /** @hide */
    public static final String FLAG_STRICT_MODE_VIOLATION_FOR_IMPLICIT_URI_GRANTS_ENABLED = "android.security.strict_mode_violation_for_implicit_uri_grants_enabled";
    /** @hide */
    public static final String FLAG_TALISMAN_SERVICE_API = "android.security.talisman_service_api";
    /** @hide */
    public static final String FLAG_UPROBESTATS_BRIDGE_SERVICE = "android.security.uprobestats_bridge_service";

    public static boolean aapmApi() {
        if (Build.VERSION.SDK_INT >= 36) {
          return true;
        }
        return FEATURE_FLAGS.aapmApi();
    }

    public static boolean aapmApiV2() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.aapmApiV2();
    }

    public static boolean aapmFeatureDisableInsecureWifiAutojoin() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.aapmFeatureDisableInsecureWifiAutojoin();
    }

    public static boolean aflApi() {
        if (Build.VERSION.SDK_INT >= 36) {
          return true;
        }
        return FEATURE_FLAGS.aflApi();
    }

    public static boolean appLockApis() {
        return FEATURE_FLAGS.appLockApis();
    }

    public static boolean asmRestrictionsEnabled() {
        if (Build.VERSION.SDK_INT >= 35) {
          return true;
        }
        return FEATURE_FLAGS.asmRestrictionsEnabled();
    }

    public static boolean autoSimPinManagement() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.autoSimPinManagement();
    }

    public static boolean certificateTransparencyConfiguration() {
        if (Build.VERSION.SDK_INT >= 36) {
          return true;
        }
        return FEATURE_FLAGS.certificateTransparencyConfiguration();
    }

    public static boolean conscryptNetworkSecurityConfig() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.conscryptNetworkSecurityConfig();
    }

    public static boolean contentUriPermissionApis() {
        if (Build.VERSION.SDK_INT >= 35) {
          return true;
        }
        return FEATURE_FLAGS.contentUriPermissionApis();
    }

    public static boolean deprecateUsesCleartextTraffic2() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.deprecateUsesCleartextTraffic2();
    }

    public static boolean dynamicInstrumentationApi() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.dynamicInstrumentationApi();
    }

    public static boolean enableDeviceCertificates() {
        return FEATURE_FLAGS.enableDeviceCertificates();
    }

    public static boolean enableIntentMatchingFlags() {
        if (Build.VERSION.SDK_INT >= 36) {
          return true;
        }
        return FEATURE_FLAGS.enableIntentMatchingFlags();
    }

    public static boolean enforceIntentFilterMatch() {
        if (Build.VERSION.SDK_INT >= 35) {
          return true;
        }
        return FEATURE_FLAGS.enforceIntentFilterMatch();
    }

    public static boolean extendAapmToA11yServices() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.extendAapmToA11yServices();
    }

    public static boolean frpEnforcement() {
        if (Build.VERSION.SDK_INT >= 35) {
          return true;
        }
        return FEATURE_FLAGS.frpEnforcement();
    }

    public static boolean fsverityApi() {
        if (Build.VERSION.SDK_INT >= 35) {
          return true;
        }
        return FEATURE_FLAGS.fsverityApi();
    }

    public static boolean keyinfoUnlockedDeviceRequired() {
        if (Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001) {
          return true;
        }
        return FEATURE_FLAGS.keyinfoUnlockedDeviceRequired();
    }

    public static boolean keystoreGrantApi() {
        if (Build.VERSION.SDK_INT >= 36) {
          return true;
        }
        return FEATURE_FLAGS.keystoreGrantApi();
    }

    public static boolean mgf1DigestSetterV2() {
        if (Build.VERSION.SDK_INT >= 35) {
          return true;
        }
        return FEATURE_FLAGS.mgf1DigestSetterV2();
    }

    public static boolean preventIntentRedirect() {
        if (Build.VERSION.SDK_INT >= 36) {
          return true;
        }
        return FEATURE_FLAGS.preventIntentRedirect();
    }

    public static boolean secureLockDevice() {
        if (Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001) {
          return true;
        }
        return FEATURE_FLAGS.secureLockDevice();
    }

    public static boolean secureLockdown() {
        if (Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001) {
          return true;
        }
        return FEATURE_FLAGS.secureLockdown();
    }

    public static boolean strictModeViolationForImplicitUriGrantsEnabled() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.strictModeViolationForImplicitUriGrantsEnabled();
    }

    public static boolean talismanServiceApi() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.talismanServiceApi();
    }

    public static boolean uprobestatsBridgeService() {
        if (Build.VERSION.SDK_INT >= 37) {
          return true;
        }
        return FEATURE_FLAGS.uprobestatsBridgeService();
    }

    private static FeatureFlags FEATURE_FLAGS = new FeatureFlagsImpl();
}
