package android.companion.virtualdevice.flags;


/** @hide */
public final class Flags {

    /** @hide */
    public static final String FLAG_ACTIVITY_CONTROL_API = "android.companion.virtualdevice.flags.activity_control_api";
    /** @hide */
    public static final String FLAG_ASSOCIATED_DISPLAY_ID_INPUT_API = "android.companion.virtualdevice.flags.associated_display_id_input_api";
    /** @hide */
    public static final String FLAG_AUDIO_FOCUS_ENVIRONMENTS = "android.companion.virtualdevice.flags.audio_focus_environments";
    /** @hide */
    public static final String FLAG_CAMERA_MULTIPLE_INPUT_STREAMS = "android.companion.virtualdevice.flags.camera_multiple_input_streams";
    /** @hide */
    public static final String FLAG_CAMERA_TIMESTAMP_FROM_SURFACE = "android.companion.virtualdevice.flags.camera_timestamp_from_surface";
    /** @hide */
    public static final String FLAG_COMPUTER_CONTROL_ACCESS = "android.companion.virtualdevice.flags.computer_control_access";
    /** @hide */
    public static final String FLAG_COMPUTER_CONTROL_CROSS_DEVICE = "android.companion.virtualdevice.flags.computer_control_cross_device";
    /** @hide */
    public static final String FLAG_COMPUTER_CONTROL_MANAGED_PROFILES = "android.companion.virtualdevice.flags.computer_control_managed_profiles";
    /** @hide */
    public static final String FLAG_COMPUTER_CONTROL_PER_APP_CONSENT = "android.companion.virtualdevice.flags.computer_control_per_app_consent";
    /** @hide */
    public static final String FLAG_COMPUTER_CONTROL_ROLE_ASSISTANT_REQUIREMENT = "android.companion.virtualdevice.flags.computer_control_role_assistant_requirement";
    /** @hide */
    public static final String FLAG_COMPUTER_CONTROL_SUPPORT_V5 = "android.companion.virtualdevice.flags.computer_control_support_v5";
    /** @hide */
    public static final String FLAG_DEFAULT_DEVICE_CAMERA_ACCESS_POLICY = "android.companion.virtualdevice.flags.default_device_camera_access_policy";
    /** @hide */
    public static final String FLAG_DEVICE_AWARE_DISPLAY_POWER = "android.companion.virtualdevice.flags.device_aware_display_power";
    /** @hide */
    public static final String FLAG_DEVICE_AWARE_THERMAL_STATUS = "android.companion.virtualdevice.flags.device_aware_thermal_status";
    /** @hide */
    public static final String FLAG_DEVICE_AWARE_TOUCH_MODE = "android.companion.virtualdevice.flags.device_aware_touch_mode";
    /** @hide */
    public static final String FLAG_DEVICE_AWARE_UI_MODE = "android.companion.virtualdevice.flags.device_aware_ui_mode";
    /** @hide */
    public static final String FLAG_ENABLE_LIMITED_VDM_ROLE = "android.companion.virtualdevice.flags.enable_limited_vdm_role";
    /** @hide */
    public static final String FLAG_EXTERNAL_CAMERA_DEFAULT_POLICY = "android.companion.virtualdevice.flags.external_camera_default_policy";
    /** @hide */
    public static final String FLAG_FIX_VDM_OPT_OUT_ON_MIRROR_DISPLAYS = "android.companion.virtualdevice.flags.fix_vdm_opt_out_on_mirror_displays";
    /** @hide */
    public static final String FLAG_MULTI_USER_VIEW_CONFIGURATION = "android.companion.virtualdevice.flags.multi_user_view_configuration";
    /** @hide */
    public static final String FLAG_NOTIFICATIONS_FOR_DEVICE_STREAMING = "android.companion.virtualdevice.flags.notifications_for_device_streaming";
    /** @hide */
    public static final String FLAG_PUBLIC_DEVICE_PROFILE = "android.companion.virtualdevice.flags.public_device_profile";
    /** @hide */
    public static final String FLAG_REMOVE_START_MODE_FROM_BLOCKED_INTENTS = "android.companion.virtualdevice.flags.remove_start_mode_from_blocked_intents";
    /** @hide */
    public static final String FLAG_STATUS_BAR_AND_INSETS = "android.companion.virtualdevice.flags.status_bar_and_insets";
    /** @hide */
    public static final String FLAG_VDM_MIRROR_DISPLAY_PERMISSION = "android.companion.virtualdevice.flags.vdm_mirror_display_permission";
    /** @hide */
    public static final String FLAG_VIEWCONFIGURATION_APIS = "android.companion.virtualdevice.flags.viewconfiguration_apis";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_CLOSE_SESSION = "android.companion.virtualdevice.flags.virtual_camera_close_session";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_DIRECT_BLOB_TRANSFER = "android.companion.virtualdevice.flags.virtual_camera_direct_blob_transfer";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_LOWER_RESOURCE_COST = "android.companion.virtualdevice.flags.virtual_camera_lower_resource_cost";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_METADATA = "android.companion.virtualdevice.flags.virtual_camera_metadata";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_NO_FRAME_DUPLICATION = "android.companion.virtualdevice.flags.virtual_camera_no_frame_duplication";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_ON_OPEN = "android.companion.virtualdevice.flags.virtual_camera_on_open";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_STABLE_STREAM_ID = "android.companion.virtualdevice.flags.virtual_camera_stable_stream_id";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_STREAM_CLOSE_DEVICE_CLOSE = "android.companion.virtualdevice.flags.virtual_camera_stream_close_device_close";
    /** @hide */
    public static final String FLAG_VIRTUAL_CAMERA_SUPPORT_API = "android.companion.virtualdevice.flags.virtual_camera_support_api";
    /** @hide */
    public static final String FLAG_VIRTUAL_DISPLAY_INSETS = "android.companion.virtualdevice.flags.virtual_display_insets";
    /** @hide */
    public static final String FLAG_VIRTUAL_DISPLAY_ROTATION_API = "android.companion.virtualdevice.flags.virtual_display_rotation_api";
    /** @hide */
    public static final String FLAG_VIRTUAL_INPUT_VIEW_BEHAVIOR = "android.companion.virtualdevice.flags.virtual_input_view_behavior";
    /** @hide */
    public static final String FLAG_VIRTUAL_ROTARY = "android.companion.virtualdevice.flags.virtual_rotary";
    /** @hide */
    public static final String FLAG_VIRTUAL_SENSOR_ADDITIONAL_INFO = "android.companion.virtualdevice.flags.virtual_sensor_additional_info";
    public static boolean activityControlApi() {
        return true;
    }
    public static boolean associatedDisplayIdInputApi() {
        return true;
    }
    public static boolean audioFocusEnvironments() {
        return true;
    }
    public static boolean cameraMultipleInputStreams() {
        return true;
    }
    public static boolean cameraTimestampFromSurface() {
        return true;
    }
    public static boolean computerControlAccess() {
        return false;
    }
    public static boolean computerControlCrossDevice() {
        return false;
    }
    public static boolean computerControlManagedProfiles() {
        return true;
    }
    public static boolean computerControlPerAppConsent() {
        return false;
    }
    public static boolean computerControlRoleAssistantRequirement() {
        return true;
    }
    public static boolean computerControlSupportV5() {
        return false;
    }
    public static boolean defaultDeviceCameraAccessPolicy() {
        return true;
    }
    public static boolean deviceAwareDisplayPower() {
        return true;
    }
    public static boolean deviceAwareThermalStatus() {
        return false;
    }
    public static boolean deviceAwareTouchMode() {
        return true;
    }
    public static boolean deviceAwareUiMode() {
        return true;
    }
    public static boolean enableLimitedVdmRole() {
        return false;
    }
    public static boolean externalCameraDefaultPolicy() {
        return false;
    }
    public static boolean fixVdmOptOutOnMirrorDisplays() {
        return false;
    }
    public static boolean multiUserViewConfiguration() {
        return true;
    }
    public static boolean notificationsForDeviceStreaming() {
        return true;
    }
    public static boolean publicDeviceProfile() {
        return false;
    }
    public static boolean removeStartModeFromBlockedIntents() {
        return true;
    }
    public static boolean statusBarAndInsets() {
        return true;
    }
    public static boolean vdmMirrorDisplayPermission() {
        return true;
    }
    public static boolean viewconfigurationApis() {
        return true;
    }
    public static boolean virtualCameraCloseSession() {
        return false;
    }
    public static boolean virtualCameraDirectBlobTransfer() {
        return true;
    }
    public static boolean virtualCameraLowerResourceCost() {
        return true;
    }
    public static boolean virtualCameraMetadata() {
        return true;
    }
    public static boolean virtualCameraNoFrameDuplication() {
        return false;
    }
    public static boolean virtualCameraOnOpen() {
        return true;
    }
    public static boolean virtualCameraStableStreamId() {
        return true;
    }
    public static boolean virtualCameraStreamCloseDeviceClose() {
        return true;
    }
    public static boolean virtualCameraSupportApi() {
        return false;
    }
    public static boolean virtualDisplayInsets() {
        return true;
    }
    public static boolean virtualDisplayRotationApi() {
        return true;
    }
    public static boolean virtualInputViewBehavior() {
        return true;
    }
    public static boolean virtualRotary() {
        return true;
    }
    public static boolean virtualSensorAdditionalInfo() {
        return true;
    }

}
