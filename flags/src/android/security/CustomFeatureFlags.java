package android.security;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import android.os.Build;
/** @hide */
public class CustomFeatureFlags implements FeatureFlags {

    private BiPredicate<String, Predicate<FeatureFlags>> mGetValueImpl;

    public CustomFeatureFlags(BiPredicate<String, Predicate<FeatureFlags>> getValueImpl) {
        mGetValueImpl = getValueImpl;
    }

    @Override
    public boolean aapmApi() {
        return getValue(Flags.FLAG_AAPM_API,
            FeatureFlags::aapmApi);
    }

    @Override
    public boolean aapmApiV2() {
        return getValue(Flags.FLAG_AAPM_API_V2,
            FeatureFlags::aapmApiV2);
    }

    @Override
    public boolean aapmFeatureDisableInsecureWifiAutojoin() {
        return getValue(Flags.FLAG_AAPM_FEATURE_DISABLE_INSECURE_WIFI_AUTOJOIN,
            FeatureFlags::aapmFeatureDisableInsecureWifiAutojoin);
    }

    @Override
    public boolean aflApi() {
        return getValue(Flags.FLAG_AFL_API,
            FeatureFlags::aflApi);
    }

    @Override
    public boolean appLockApis() {
        return getValue(Flags.FLAG_APP_LOCK_APIS,
            FeatureFlags::appLockApis);
    }

    @Override
    public boolean asmRestrictionsEnabled() {
        return getValue(Flags.FLAG_ASM_RESTRICTIONS_ENABLED,
            FeatureFlags::asmRestrictionsEnabled);
    }

    @Override
    public boolean autoSimPinManagement() {
        return getValue(Flags.FLAG_AUTO_SIM_PIN_MANAGEMENT,
            FeatureFlags::autoSimPinManagement);
    }

    @Override
    public boolean certificateTransparencyConfiguration() {
        return getValue(Flags.FLAG_CERTIFICATE_TRANSPARENCY_CONFIGURATION,
            FeatureFlags::certificateTransparencyConfiguration);
    }

    @Override
    public boolean conscryptNetworkSecurityConfig() {
        return getValue(Flags.FLAG_CONSCRYPT_NETWORK_SECURITY_CONFIG,
            FeatureFlags::conscryptNetworkSecurityConfig);
    }

    @Override
    public boolean contentUriPermissionApis() {
        return getValue(Flags.FLAG_CONTENT_URI_PERMISSION_APIS,
            FeatureFlags::contentUriPermissionApis);
    }

    @Override
    public boolean deprecateUsesCleartextTraffic2() {
        return getValue(Flags.FLAG_DEPRECATE_USES_CLEARTEXT_TRAFFIC2,
            FeatureFlags::deprecateUsesCleartextTraffic2);
    }

    @Override
    public boolean dynamicInstrumentationApi() {
        return getValue(Flags.FLAG_DYNAMIC_INSTRUMENTATION_API,
            FeatureFlags::dynamicInstrumentationApi);
    }

    @Override
    public boolean enableDeviceCertificates() {
        return getValue(Flags.FLAG_ENABLE_DEVICE_CERTIFICATES,
            FeatureFlags::enableDeviceCertificates);
    }

    @Override
    public boolean enableIntentMatchingFlags() {
        return getValue(Flags.FLAG_ENABLE_INTENT_MATCHING_FLAGS,
            FeatureFlags::enableIntentMatchingFlags);
    }

    @Override
    public boolean enforceIntentFilterMatch() {
        return getValue(Flags.FLAG_ENFORCE_INTENT_FILTER_MATCH,
            FeatureFlags::enforceIntentFilterMatch);
    }

    @Override
    public boolean extendAapmToA11yServices() {
        return getValue(Flags.FLAG_EXTEND_AAPM_TO_A11Y_SERVICES,
            FeatureFlags::extendAapmToA11yServices);
    }

    @Override
    public boolean frpEnforcement() {
        return getValue(Flags.FLAG_FRP_ENFORCEMENT,
            FeatureFlags::frpEnforcement);
    }

    @Override
    public boolean fsverityApi() {
        return getValue(Flags.FLAG_FSVERITY_API,
            FeatureFlags::fsverityApi);
    }

    @Override
    public boolean keyinfoUnlockedDeviceRequired() {
        return getValue(Flags.FLAG_KEYINFO_UNLOCKED_DEVICE_REQUIRED,
            FeatureFlags::keyinfoUnlockedDeviceRequired);
    }

    @Override
    public boolean keystoreGrantApi() {
        return getValue(Flags.FLAG_KEYSTORE_GRANT_API,
            FeatureFlags::keystoreGrantApi);
    }

    @Override
    public boolean mgf1DigestSetterV2() {
        return getValue(Flags.FLAG_MGF1_DIGEST_SETTER_V2,
            FeatureFlags::mgf1DigestSetterV2);
    }

    @Override
    public boolean preventIntentRedirect() {
        return getValue(Flags.FLAG_PREVENT_INTENT_REDIRECT,
            FeatureFlags::preventIntentRedirect);
    }

    @Override
    public boolean secureLockDevice() {
        return getValue(Flags.FLAG_SECURE_LOCK_DEVICE,
            FeatureFlags::secureLockDevice);
    }

    @Override
    public boolean secureLockdown() {
        return getValue(Flags.FLAG_SECURE_LOCKDOWN,
            FeatureFlags::secureLockdown);
    }

    @Override
    public boolean strictModeViolationForImplicitUriGrantsEnabled() {
        return getValue(Flags.FLAG_STRICT_MODE_VIOLATION_FOR_IMPLICIT_URI_GRANTS_ENABLED,
            FeatureFlags::strictModeViolationForImplicitUriGrantsEnabled);
    }

    @Override
    public boolean talismanServiceApi() {
        return getValue(Flags.FLAG_TALISMAN_SERVICE_API,
            FeatureFlags::talismanServiceApi);
    }

    @Override
    public boolean uprobestatsBridgeService() {
        return getValue(Flags.FLAG_UPROBESTATS_BRIDGE_SERVICE,
            FeatureFlags::uprobestatsBridgeService);
    }


    protected boolean getValue(String flagName, Predicate<FeatureFlags> getter) {
        return mGetValueImpl.test(flagName, getter);
    }

    public List<String> getFlagNames() {
        return Arrays.asList(
            Flags.FLAG_AAPM_API,
            Flags.FLAG_AAPM_API_V2,
            Flags.FLAG_AAPM_FEATURE_DISABLE_INSECURE_WIFI_AUTOJOIN,
            Flags.FLAG_AFL_API,
            Flags.FLAG_APP_LOCK_APIS,
            Flags.FLAG_ASM_RESTRICTIONS_ENABLED,
            Flags.FLAG_AUTO_SIM_PIN_MANAGEMENT,
            Flags.FLAG_CERTIFICATE_TRANSPARENCY_CONFIGURATION,
            Flags.FLAG_CONSCRYPT_NETWORK_SECURITY_CONFIG,
            Flags.FLAG_CONTENT_URI_PERMISSION_APIS,
            Flags.FLAG_DEPRECATE_USES_CLEARTEXT_TRAFFIC2,
            Flags.FLAG_DYNAMIC_INSTRUMENTATION_API,
            Flags.FLAG_ENABLE_DEVICE_CERTIFICATES,
            Flags.FLAG_ENABLE_INTENT_MATCHING_FLAGS,
            Flags.FLAG_ENFORCE_INTENT_FILTER_MATCH,
            Flags.FLAG_EXTEND_AAPM_TO_A11Y_SERVICES,
            Flags.FLAG_FRP_ENFORCEMENT,
            Flags.FLAG_FSVERITY_API,
            Flags.FLAG_KEYINFO_UNLOCKED_DEVICE_REQUIRED,
            Flags.FLAG_KEYSTORE_GRANT_API,
            Flags.FLAG_MGF1_DIGEST_SETTER_V2,
            Flags.FLAG_PREVENT_INTENT_REDIRECT,
            Flags.FLAG_SECURE_LOCK_DEVICE,
            Flags.FLAG_SECURE_LOCKDOWN,
            Flags.FLAG_STRICT_MODE_VIOLATION_FOR_IMPLICIT_URI_GRANTS_ENABLED,
            Flags.FLAG_TALISMAN_SERVICE_API,
            Flags.FLAG_UPROBESTATS_BRIDGE_SERVICE
        );
    }

    private Set<String> mReadOnlyFlagsSet = new HashSet<>(
        Arrays.asList(
            ""
        )
    );

    private Map<String, Boolean> mFinalizedFlags = new HashMap<>(
        Map.ofEntries(
            Map.entry(Flags.FLAG_AAPM_API, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_AAPM_API_V2, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_AAPM_FEATURE_DISABLE_INSECURE_WIFI_AUTOJOIN, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_AFL_API, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ASM_RESTRICTIONS_ENABLED, Build.VERSION.SDK_INT >= 35 ? true : false),
            Map.entry(Flags.FLAG_AUTO_SIM_PIN_MANAGEMENT, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_CERTIFICATE_TRANSPARENCY_CONFIGURATION, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_CONSCRYPT_NETWORK_SECURITY_CONFIG, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_CONTENT_URI_PERMISSION_APIS, Build.VERSION.SDK_INT >= 35 ? true : false),
            Map.entry(Flags.FLAG_DEPRECATE_USES_CLEARTEXT_TRAFFIC2, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_DYNAMIC_INSTRUMENTATION_API, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_INTENT_MATCHING_FLAGS, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENFORCE_INTENT_FILTER_MATCH, Build.VERSION.SDK_INT >= 35 ? true : false),
            Map.entry(Flags.FLAG_EXTEND_AAPM_TO_A11Y_SERVICES, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_FRP_ENFORCEMENT, Build.VERSION.SDK_INT >= 35 ? true : false),
            Map.entry(Flags.FLAG_FSVERITY_API, Build.VERSION.SDK_INT >= 35 ? true : false),
            Map.entry(Flags.FLAG_KEYINFO_UNLOCKED_DEVICE_REQUIRED, Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : false),
            Map.entry(Flags.FLAG_KEYSTORE_GRANT_API, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_MGF1_DIGEST_SETTER_V2, Build.VERSION.SDK_INT >= 35 ? true : false),
            Map.entry(Flags.FLAG_PREVENT_INTENT_REDIRECT, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_SECURE_LOCK_DEVICE, Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : false),
            Map.entry(Flags.FLAG_SECURE_LOCKDOWN, Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : false),
            Map.entry(Flags.FLAG_STRICT_MODE_VIOLATION_FOR_IMPLICIT_URI_GRANTS_ENABLED, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_TALISMAN_SERVICE_API, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_UPROBESTATS_BRIDGE_SERVICE, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry("", false)
        )
    );

    public boolean isFlagFinalized(String flagName) {
        if (!mFinalizedFlags.containsKey(flagName)) {
            return false;
        }
        return mFinalizedFlags.get(flagName);
    }

}
