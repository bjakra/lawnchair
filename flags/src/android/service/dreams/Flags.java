package android.service.dreams;


/** @hide */
public final class Flags {

    /** @hide */
    public static final String FLAG_ALLOW_DREAM_WITH_CHARGE_LIMIT = "android.service.dreams.allow_dream_with_charge_limit";
    /** @hide */
    public static final String FLAG_CLEANUP_DREAM_SETTINGS_ON_UNINSTALL = "android.service.dreams.cleanup_dream_settings_on_uninstall";
    /** @hide */
    public static final String FLAG_DISMISS_DREAM_ON_KEYGUARD_DISMISS = "android.service.dreams.dismiss_dream_on_keyguard_dismiss";
    /** @hide */
    public static final String FLAG_DREAM_HANDLES_BEING_OBSCURED = "android.service.dreams.dream_handles_being_obscured";
    /** @hide */
    public static final String FLAG_DREAM_HANDLES_CONFIRM_KEYS = "android.service.dreams.dream_handles_confirm_keys";
    /** @hide */
    public static final String FLAG_DREAM_OVERLAY_HOST = "android.service.dreams.dream_overlay_host";
    /** @hide */
    public static final String FLAG_DREAM_WAKE_REDIRECT = "android.service.dreams.dream_wake_redirect";
    /** @hide */
    public static final String FLAG_DREAMS_QUERY_APPLICATION_INFO = "android.service.dreams.dreams_query_application_info";
    /** @hide */
    public static final String FLAG_DREAMS_SWITCHER = "android.service.dreams.dreams_switcher";
    /** @hide */
    public static final String FLAG_DREAMS_SWITCHER_EDGE_SWIPE_ENABLED = "android.service.dreams.dreams_switcher_edge_swipe_enabled";
    /** @hide */
    public static final String FLAG_DREAMS_SWITCHER_LONG_PRESS_ENABLED = "android.service.dreams.dreams_switcher_long_press_enabled";
    /** @hide */
    public static final String FLAG_DREAMS_V2 = "android.service.dreams.dreams_v2";
    /** @hide */
    public static final String FLAG_DRIVE_DREAM_STATE_FROM_OCCLUSION = "android.service.dreams.drive_dream_state_from_occlusion";
    /** @hide */
    public static final String FLAG_NAP_WHEN_DREAM_ENABLED = "android.service.dreams.nap_when_dream_enabled";
    /** @hide */
    public static final String FLAG_PUBLISH_PREVIEW_STATE_TO_OVERLAY = "android.service.dreams.publish_preview_state_to_overlay";
    /** @hide */
    public static final String FLAG_SYSTEM_DREAM_DEATH_RECIPIENT = "android.service.dreams.system_dream_death_recipient";
    /** @hide */
    public static final String FLAG_USER_SELECTABLE_METADATA = "android.service.dreams.user_selectable_metadata";
    public static boolean allowDreamWithChargeLimit() {
        return false;
    }
    public static boolean cleanupDreamSettingsOnUninstall() {
        return true;
    }
    public static boolean dismissDreamOnKeyguardDismiss() {
        return true;
    }
    public static boolean dreamHandlesBeingObscured() {
        return true;
    }
    public static boolean dreamHandlesConfirmKeys() {
        return true;
    }
    public static boolean dreamOverlayHost() {
        return false;
    }
    public static boolean dreamWakeRedirect() {
        return true;
    }
    public static boolean dreamsQueryApplicationInfo() {
        return true;
    }
    public static boolean dreamsSwitcher() {
        return false;
    }
    public static boolean dreamsSwitcherEdgeSwipeEnabled() {
        return false;
    }
    public static boolean dreamsSwitcherLongPressEnabled() {
        return false;
    }
    public static boolean dreamsV2() {
        return true;
    }
    public static boolean driveDreamStateFromOcclusion() {
        return false;
    }
    public static boolean napWhenDreamEnabled() {
        return false;
    }
    public static boolean publishPreviewStateToOverlay() {
        return true;
    }
    public static boolean systemDreamDeathRecipient() {
        return true;
    }
    public static boolean userSelectableMetadata() {
        return true;
    }

}
