package android.companion;


/** @hide */
public final class Flags {

    /** @hide */
    public static final String FLAG_ASSOCIATION_DEVICE_ICON = "android.companion.association_device_icon";
    /** @hide */
    public static final String FLAG_ASSOCIATION_EXTRA_PERMISSION = "android.companion.association_extra_permission";
    /** @hide */
    public static final String FLAG_ASSOCIATION_FAILURE_CODE = "android.companion.association_failure_code";
    /** @hide */
    public static final String FLAG_ASSOCIATION_TAG = "android.companion.association_tag";
    /** @hide */
    public static final String FLAG_ASSOCIATION_VERIFICATION = "android.companion.association_verification";
    /** @hide */
    public static final String FLAG_BAND_DEVICE_PROFILE = "android.companion.band_device_profile";
    /** @hide */
    public static final String FLAG_CMD_OPTIONS = "android.companion.cmd_options";
    /** @hide */
    public static final String FLAG_DEVICE_PRESENCE = "android.companion.device_presence";
    /** @hide */
    public static final String FLAG_ENABLE_DATA_SYNC = "android.companion.enable_data_sync";
    /** @hide */
    public static final String FLAG_ENABLE_MEDICAL_PROFILE = "android.companion.enable_medical_profile";
    /** @hide */
    public static final String FLAG_ENABLE_REMOTE_APP_ACCESS = "android.companion.enable_remote_app_access";
    /** @hide */
    public static final String FLAG_EXPAND_APP_STREAMING_ROLE_PERMISSIONS = "android.companion.expand_app_streaming_role_permissions";
    /** @hide */
    public static final String FLAG_NEW_ASSOCIATION_BUILDER = "android.companion.new_association_builder";
    /** @hide */
    public static final String FLAG_NOTIFY_ASSOCIATION_REMOVED = "android.companion.notify_association_removed";
    /** @hide */
    public static final String FLAG_ONGOING_PERM_SYNC = "android.companion.ongoing_perm_sync";
    /** @hide */
    public static final String FLAG_PERM_SYNC_USER_CONSENT = "android.companion.perm_sync_user_consent";
    /** @hide */
    public static final String FLAG_SUPPORT_AI_AGENT = "android.companion.support_ai_agent";
    /** @hide */
    public static final String FLAG_TASK_CONTINUITY = "android.companion.task_continuity";
    /** @hide */
    public static final String FLAG_TRUSTED_DEVICES = "android.companion.trusted_devices";
    /** @hide */
    public static final String FLAG_UNIVERSAL_CLIPBOARD = "android.companion.universal_clipboard";
    /** @hide */
    public static final String FLAG_UNPAIR_ASSOCIATED_DEVICE = "android.companion.unpair_associated_device";
    public static boolean associationDeviceIcon() {
        return true;
    }
    public static boolean associationExtraPermission() {
        return true;
    }
    public static boolean associationFailureCode() {
        return true;
    }
    public static boolean associationTag() {
        return true;
    }
    public static boolean associationVerification() {
        return true;
    }
    public static boolean bandDeviceProfile() {
        return true;
    }
    public static boolean cmdOptions() {
        return false;
    }
    public static boolean devicePresence() {
        return true;
    }
    public static boolean enableDataSync() {
        return true;
    }
    public static boolean enableMedicalProfile() {
        return true;
    }
    public static boolean enableRemoteAppAccess() {
        return false;
    }
    public static boolean expandAppStreamingRolePermissions() {
        return true;
    }
    public static boolean newAssociationBuilder() {
        return true;
    }
    public static boolean notifyAssociationRemoved() {
        return true;
    }
    public static boolean ongoingPermSync() {
        return false;
    }
    public static boolean permSyncUserConsent() {
        return true;
    }
    public static boolean supportAiAgent() {
        return true;
    }
    public static boolean taskContinuity() {
        return true;
    }
    public static boolean trustedDevices() {
        return true;
    }
    public static boolean universalClipboard() {
        return false;
    }
    public static boolean unpairAssociatedDevice() {
        return true;
    }

}
