package com.android.providers.media.flags;

import android.os.Build;
import android.os.flagging.AconfigPackage;
import android.util.Log;
/** @hide */
final class FeatureFlagsImpl implements FeatureFlags {
    private static final String TAG = "FeatureFlagsImplExport";
    private static volatile boolean isCached = false;

    private static boolean accessMediaOwnerPackageNamePermission = false;
    private static boolean audioSampleColumns = false;
    private static boolean cloudMediaProviderSearch = false;
    private static boolean cloudProviderAlbumsAsCategoryApi = false;
    private static boolean enableBackupAndRestore = false;
    private static boolean enableCloudMediaProviderCapabilities = false;
    private static boolean enableEmbeddedPhotopicker = false;
    private static boolean enableEmbeddedPickerExpandedHighlightTypeApi = false;
    private static boolean enableExclusionListForDefaultFolders = false;
    private static boolean enableLocalMediaProviderCapabilities = false;
    private static boolean enableMarkIsFavoriteStatusApi = false;
    private static boolean enableMarkMediaAsFavoriteApi = false;
    private static boolean enableMediaProcessingService = false;
    private static boolean enableMediaSearch = false;
    private static boolean enableMimeTypeFixForAndroid15 = false;
    private static boolean enableModernPhotopicker = false;
    private static boolean enableOemMetadata = false;
    private static boolean enableOemMetadataUpdate = false;
    private static boolean enableOemMetadataUsingMimetype = false;
    private static boolean enablePhotopickerDatescrubber = false;
    private static boolean enablePhotopickerSearch = false;
    private static boolean enablePhotopickerSelectionParamsApi = false;
    private static boolean enablePhotopickerTranscoding = false;
    private static boolean enablePhotopickerUiCustomizationParamsApi = false;
    private static boolean enablePickerHighlightSearchResultsApis = false;
    private static boolean enablePickerLocationMetadataApi = false;
    private static boolean enableSpecialFormatColumn = false;
    private static boolean enableStableUrisForExternalPrimaryVolume = false;
    private static boolean enableStableUrisForPublicVolume = false;
    private static boolean enableTrashAndRestoreByFilePathApi = false;
    private static boolean enableUnicodeCheck = false;
    private static boolean excludeUnreliableVolumes = false;
    private static boolean indexMediaLatitudeLongitude = false;
    private static boolean inferredMediaDate = false;
    private static boolean mediaCognitionService = false;
    private static boolean mediaStoreOpenFile = false;
    private static boolean motionPhotoIntent = false;
    private static boolean pickOrderedImages = false;
    private static boolean pickerAccentColor = false;
    private static boolean pickerDefaultTab = false;
    private static boolean pickerPreSelectionExtra = false;
    private static boolean pickerRecentSelection = false;
    private static boolean revokeAccessOwnedPhotos = false;
    private static boolean versionLockdown = false;
    private void init() {
        try {
            AconfigPackage reader = AconfigPackage.load("com.android.providers.media.flags");
            accessMediaOwnerPackageNamePermission = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("access_media_owner_package_name_permission", false);
            audioSampleColumns = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("audio_sample_columns", false);
            cloudMediaProviderSearch = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("cloud_media_provider_search", false);
            cloudProviderAlbumsAsCategoryApi = Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : reader.getBooleanFlagValue("cloud_provider_albums_as_category_api", false);
            enableBackupAndRestore = reader.getBooleanFlagValue("enable_backup_and_restore", false);
            enableCloudMediaProviderCapabilities = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_cloud_media_provider_capabilities", false);
            enableEmbeddedPhotopicker = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_embedded_photopicker", false);
            enableEmbeddedPickerExpandedHighlightTypeApi = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("enable_embedded_picker_expanded_highlight_type_api", false);
            enableExclusionListForDefaultFolders = reader.getBooleanFlagValue("enable_exclusion_list_for_default_folders", false);
            enableLocalMediaProviderCapabilities = reader.getBooleanFlagValue("enable_local_media_provider_capabilities", false);
            enableMarkIsFavoriteStatusApi = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_mark_is_favorite_status_api", false);
            enableMarkMediaAsFavoriteApi = reader.getBooleanFlagValue("enable_mark_media_as_favorite_api", false);
            enableMediaProcessingService = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("enable_media_processing_service", false);
            enableMediaSearch = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("enable_media_search", false);
            enableMimeTypeFixForAndroid15 = reader.getBooleanFlagValue("enable_mime_type_fix_for_android_15", false);
            enableModernPhotopicker = reader.getBooleanFlagValue("enable_modern_photopicker", false);
            enableOemMetadata = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_oem_metadata", false);
            enableOemMetadataUpdate = Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : reader.getBooleanFlagValue("enable_oem_metadata_update", false);
            enableOemMetadataUsingMimetype = Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : reader.getBooleanFlagValue("enable_oem_metadata_using_mimetype", false);
            enablePhotopickerDatescrubber = reader.getBooleanFlagValue("enable_photopicker_datescrubber", false);
            enablePhotopickerSearch = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_photopicker_search", false);
            enablePhotopickerSelectionParamsApi = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("enable_photopicker_selection_params_api", false);
            enablePhotopickerTranscoding = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_photopicker_transcoding", false);
            enablePhotopickerUiCustomizationParamsApi = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("enable_photopicker_ui_customization_params_api", false);
            enablePickerHighlightSearchResultsApis = Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : reader.getBooleanFlagValue("enable_picker_highlight_search_results_apis", false);
            enablePickerLocationMetadataApi = reader.getBooleanFlagValue("enable_picker_location_metadata_api", false);
            enableSpecialFormatColumn = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("enable_special_format_column", false);
            enableStableUrisForExternalPrimaryVolume = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_stable_uris_for_external_primary_volume", false);
            enableStableUrisForPublicVolume = reader.getBooleanFlagValue("enable_stable_uris_for_public_volume", false);
            enableTrashAndRestoreByFilePathApi = Build.VERSION.SDK_INT >= 37 ? true : reader.getBooleanFlagValue("enable_trash_and_restore_by_file_path_api", false);
            enableUnicodeCheck = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("enable_unicode_check", false);
            excludeUnreliableVolumes = reader.getBooleanFlagValue("exclude_unreliable_volumes", false);
            indexMediaLatitudeLongitude = reader.getBooleanFlagValue("index_media_latitude_longitude", false);
            inferredMediaDate = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("inferred_media_date", false);
            mediaCognitionService = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("media_cognition_service", false);
            mediaStoreOpenFile = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("media_store_open_file", false);
            motionPhotoIntent = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("motion_photo_intent", false);
            pickOrderedImages = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("pick_ordered_images", false);
            pickerAccentColor = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("picker_accent_color", false);
            pickerDefaultTab = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("picker_default_tab", false);
            pickerPreSelectionExtra = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("picker_pre_selection_extra", false);
            pickerRecentSelection = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("picker_recent_selection", false);
            revokeAccessOwnedPhotos = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("revoke_access_owned_photos", false);
            versionLockdown = Build.VERSION.SDK_INT >= 36 ? true : reader.getBooleanFlagValue("version_lockdown", false);
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
    public boolean accessMediaOwnerPackageNamePermission() {
        if (!isCached) {
            init();
        }
        return accessMediaOwnerPackageNamePermission;
    }

    @Override
    public boolean audioSampleColumns() {
        if (!isCached) {
            init();
        }
        return audioSampleColumns;
    }

    @Override
    public boolean cloudMediaProviderSearch() {
        if (!isCached) {
            init();
        }
        return cloudMediaProviderSearch;
    }

    @Override
    public boolean cloudProviderAlbumsAsCategoryApi() {
        if (!isCached) {
            init();
        }
        return cloudProviderAlbumsAsCategoryApi;
    }

    @Override
    public boolean enableBackupAndRestore() {
        if (!isCached) {
            init();
        }
        return enableBackupAndRestore;
    }

    @Override
    public boolean enableCloudMediaProviderCapabilities() {
        if (!isCached) {
            init();
        }
        return enableCloudMediaProviderCapabilities;
    }

    @Override
    public boolean enableEmbeddedPhotopicker() {
        if (!isCached) {
            init();
        }
        return enableEmbeddedPhotopicker;
    }

    @Override
    public boolean enableEmbeddedPickerExpandedHighlightTypeApi() {
        if (!isCached) {
            init();
        }
        return enableEmbeddedPickerExpandedHighlightTypeApi;
    }

    @Override
    public boolean enableExclusionListForDefaultFolders() {
        if (!isCached) {
            init();
        }
        return enableExclusionListForDefaultFolders;
    }

    @Override
    public boolean enableLocalMediaProviderCapabilities() {
        if (!isCached) {
            init();
        }
        return enableLocalMediaProviderCapabilities;
    }

    @Override
    public boolean enableMarkIsFavoriteStatusApi() {
        if (!isCached) {
            init();
        }
        return enableMarkIsFavoriteStatusApi;
    }

    @Override
    public boolean enableMarkMediaAsFavoriteApi() {
        if (!isCached) {
            init();
        }
        return enableMarkMediaAsFavoriteApi;
    }

    @Override
    public boolean enableMediaProcessingService() {
        if (!isCached) {
            init();
        }
        return enableMediaProcessingService;
    }

    @Override
    public boolean enableMediaSearch() {
        if (!isCached) {
            init();
        }
        return enableMediaSearch;
    }

    @Override
    public boolean enableMimeTypeFixForAndroid15() {
        if (!isCached) {
            init();
        }
        return enableMimeTypeFixForAndroid15;
    }

    @Override
    public boolean enableModernPhotopicker() {
        if (!isCached) {
            init();
        }
        return enableModernPhotopicker;
    }

    @Override
    public boolean enableOemMetadata() {
        if (!isCached) {
            init();
        }
        return enableOemMetadata;
    }

    @Override
    public boolean enableOemMetadataUpdate() {
        if (!isCached) {
            init();
        }
        return enableOemMetadataUpdate;
    }

    @Override
    public boolean enableOemMetadataUsingMimetype() {
        if (!isCached) {
            init();
        }
        return enableOemMetadataUsingMimetype;
    }

    @Override
    public boolean enablePhotopickerDatescrubber() {
        if (!isCached) {
            init();
        }
        return enablePhotopickerDatescrubber;
    }

    @Override
    public boolean enablePhotopickerSearch() {
        if (!isCached) {
            init();
        }
        return enablePhotopickerSearch;
    }

    @Override
    public boolean enablePhotopickerSelectionParamsApi() {
        if (!isCached) {
            init();
        }
        return enablePhotopickerSelectionParamsApi;
    }

    @Override
    public boolean enablePhotopickerTranscoding() {
        if (!isCached) {
            init();
        }
        return enablePhotopickerTranscoding;
    }

    @Override
    public boolean enablePhotopickerUiCustomizationParamsApi() {
        if (!isCached) {
            init();
        }
        return enablePhotopickerUiCustomizationParamsApi;
    }

    @Override
    public boolean enablePickerHighlightSearchResultsApis() {
        if (!isCached) {
            init();
        }
        return enablePickerHighlightSearchResultsApis;
    }

    @Override
    public boolean enablePickerLocationMetadataApi() {
        if (!isCached) {
            init();
        }
        return enablePickerLocationMetadataApi;
    }

    @Override
    public boolean enableSpecialFormatColumn() {
        if (!isCached) {
            init();
        }
        return enableSpecialFormatColumn;
    }

    @Override
    public boolean enableStableUrisForExternalPrimaryVolume() {
        if (!isCached) {
            init();
        }
        return enableStableUrisForExternalPrimaryVolume;
    }

    @Override
    public boolean enableStableUrisForPublicVolume() {
        if (!isCached) {
            init();
        }
        return enableStableUrisForPublicVolume;
    }

    @Override
    public boolean enableTrashAndRestoreByFilePathApi() {
        if (!isCached) {
            init();
        }
        return enableTrashAndRestoreByFilePathApi;
    }

    @Override
    public boolean enableUnicodeCheck() {
        if (!isCached) {
            init();
        }
        return enableUnicodeCheck;
    }

    @Override
    public boolean excludeUnreliableVolumes() {
        if (!isCached) {
            init();
        }
        return excludeUnreliableVolumes;
    }

    @Override
    public boolean indexMediaLatitudeLongitude() {
        if (!isCached) {
            init();
        }
        return indexMediaLatitudeLongitude;
    }

    @Override
    public boolean inferredMediaDate() {
        if (!isCached) {
            init();
        }
        return inferredMediaDate;
    }

    @Override
    public boolean mediaCognitionService() {
        if (!isCached) {
            init();
        }
        return mediaCognitionService;
    }

    @Override
    public boolean mediaStoreOpenFile() {
        if (!isCached) {
            init();
        }
        return mediaStoreOpenFile;
    }

    @Override
    public boolean motionPhotoIntent() {
        if (!isCached) {
            init();
        }
        return motionPhotoIntent;
    }

    @Override
    public boolean pickOrderedImages() {
        if (!isCached) {
            init();
        }
        return pickOrderedImages;
    }

    @Override
    public boolean pickerAccentColor() {
        if (!isCached) {
            init();
        }
        return pickerAccentColor;
    }

    @Override
    public boolean pickerDefaultTab() {
        if (!isCached) {
            init();
        }
        return pickerDefaultTab;
    }

    @Override
    public boolean pickerPreSelectionExtra() {
        if (!isCached) {
            init();
        }
        return pickerPreSelectionExtra;
    }

    @Override
    public boolean pickerRecentSelection() {
        if (!isCached) {
            init();
        }
        return pickerRecentSelection;
    }

    @Override
    public boolean revokeAccessOwnedPhotos() {
        if (!isCached) {
            init();
        }
        return revokeAccessOwnedPhotos;
    }

    @Override
    public boolean versionLockdown() {
        if (!isCached) {
            init();
        }
        return versionLockdown;
    }

}
