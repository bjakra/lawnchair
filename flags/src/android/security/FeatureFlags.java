package android.security;

/** @hide */
public interface FeatureFlags {

    boolean aapmApi();

    boolean aapmApiV2();

    boolean aapmFeatureDisableInsecureWifiAutojoin();

    boolean aflApi();

    boolean appLockApis();

    boolean asmRestrictionsEnabled();

    boolean autoSimPinManagement();

    boolean certificateTransparencyConfiguration();

    boolean conscryptNetworkSecurityConfig();

    boolean contentUriPermissionApis();

    boolean deprecateUsesCleartextTraffic2();

    boolean dynamicInstrumentationApi();

    boolean enableDeviceCertificates();

    boolean enableIntentMatchingFlags();

    boolean enforceIntentFilterMatch();

    boolean extendAapmToA11yServices();

    boolean frpEnforcement();

    boolean fsverityApi();

    boolean keyinfoUnlockedDeviceRequired();

    boolean keystoreGrantApi();

    boolean mgf1DigestSetterV2();

    boolean preventIntentRedirect();

    boolean secureLockDevice();

    boolean secureLockdown();

    boolean strictModeViolationForImplicitUriGrantsEnabled();

    boolean talismanServiceApi();

    boolean uprobestatsBridgeService();

}
