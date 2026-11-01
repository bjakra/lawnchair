package com.android.providers.media.flags;


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
    public boolean accessMediaOwnerPackageNamePermission() {
        return getValue(Flags.FLAG_ACCESS_MEDIA_OWNER_PACKAGE_NAME_PERMISSION,
            FeatureFlags::accessMediaOwnerPackageNamePermission);
    }

    @Override
    public boolean audioSampleColumns() {
        return getValue(Flags.FLAG_AUDIO_SAMPLE_COLUMNS,
            FeatureFlags::audioSampleColumns);
    }

    @Override
    public boolean cloudMediaProviderSearch() {
        return getValue(Flags.FLAG_CLOUD_MEDIA_PROVIDER_SEARCH,
            FeatureFlags::cloudMediaProviderSearch);
    }

    @Override
    public boolean cloudProviderAlbumsAsCategoryApi() {
        return getValue(Flags.FLAG_CLOUD_PROVIDER_ALBUMS_AS_CATEGORY_API,
            FeatureFlags::cloudProviderAlbumsAsCategoryApi);
    }

    @Override
    public boolean enableBackupAndRestore() {
        return getValue(Flags.FLAG_ENABLE_BACKUP_AND_RESTORE,
            FeatureFlags::enableBackupAndRestore);
    }

    @Override
    public boolean enableCloudMediaProviderCapabilities() {
        return getValue(Flags.FLAG_ENABLE_CLOUD_MEDIA_PROVIDER_CAPABILITIES,
            FeatureFlags::enableCloudMediaProviderCapabilities);
    }

    @Override
    public boolean enableEmbeddedPhotopicker() {
        return getValue(Flags.FLAG_ENABLE_EMBEDDED_PHOTOPICKER,
            FeatureFlags::enableEmbeddedPhotopicker);
    }

    @Override
    public boolean enableEmbeddedPickerExpandedHighlightTypeApi() {
        return getValue(Flags.FLAG_ENABLE_EMBEDDED_PICKER_EXPANDED_HIGHLIGHT_TYPE_API,
            FeatureFlags::enableEmbeddedPickerExpandedHighlightTypeApi);
    }

    @Override
    public boolean enableExclusionListForDefaultFolders() {
        return getValue(Flags.FLAG_ENABLE_EXCLUSION_LIST_FOR_DEFAULT_FOLDERS,
            FeatureFlags::enableExclusionListForDefaultFolders);
    }

    @Override
    public boolean enableLocalMediaProviderCapabilities() {
        return getValue(Flags.FLAG_ENABLE_LOCAL_MEDIA_PROVIDER_CAPABILITIES,
            FeatureFlags::enableLocalMediaProviderCapabilities);
    }

    @Override
    public boolean enableMarkIsFavoriteStatusApi() {
        return getValue(Flags.FLAG_ENABLE_MARK_IS_FAVORITE_STATUS_API,
            FeatureFlags::enableMarkIsFavoriteStatusApi);
    }

    @Override
    public boolean enableMarkMediaAsFavoriteApi() {
        return getValue(Flags.FLAG_ENABLE_MARK_MEDIA_AS_FAVORITE_API,
            FeatureFlags::enableMarkMediaAsFavoriteApi);
    }

    @Override
    public boolean enableMediaProcessingService() {
        return getValue(Flags.FLAG_ENABLE_MEDIA_PROCESSING_SERVICE,
            FeatureFlags::enableMediaProcessingService);
    }

    @Override
    public boolean enableMediaSearch() {
        return getValue(Flags.FLAG_ENABLE_MEDIA_SEARCH,
            FeatureFlags::enableMediaSearch);
    }

    @Override
    public boolean enableMimeTypeFixForAndroid15() {
        return getValue(Flags.FLAG_ENABLE_MIME_TYPE_FIX_FOR_ANDROID_15,
            FeatureFlags::enableMimeTypeFixForAndroid15);
    }

    @Override
    public boolean enableModernPhotopicker() {
        return getValue(Flags.FLAG_ENABLE_MODERN_PHOTOPICKER,
            FeatureFlags::enableModernPhotopicker);
    }

    @Override
    public boolean enableOemMetadata() {
        return getValue(Flags.FLAG_ENABLE_OEM_METADATA,
            FeatureFlags::enableOemMetadata);
    }

    @Override
    public boolean enableOemMetadataUpdate() {
        return getValue(Flags.FLAG_ENABLE_OEM_METADATA_UPDATE,
            FeatureFlags::enableOemMetadataUpdate);
    }

    @Override
    public boolean enableOemMetadataUsingMimetype() {
        return getValue(Flags.FLAG_ENABLE_OEM_METADATA_USING_MIMETYPE,
            FeatureFlags::enableOemMetadataUsingMimetype);
    }

    @Override
    public boolean enablePhotopickerDatescrubber() {
        return getValue(Flags.FLAG_ENABLE_PHOTOPICKER_DATESCRUBBER,
            FeatureFlags::enablePhotopickerDatescrubber);
    }

    @Override
    public boolean enablePhotopickerSearch() {
        return getValue(Flags.FLAG_ENABLE_PHOTOPICKER_SEARCH,
            FeatureFlags::enablePhotopickerSearch);
    }

    @Override
    public boolean enablePhotopickerSelectionParamsApi() {
        return getValue(Flags.FLAG_ENABLE_PHOTOPICKER_SELECTION_PARAMS_API,
            FeatureFlags::enablePhotopickerSelectionParamsApi);
    }

    @Override
    public boolean enablePhotopickerTranscoding() {
        return getValue(Flags.FLAG_ENABLE_PHOTOPICKER_TRANSCODING,
            FeatureFlags::enablePhotopickerTranscoding);
    }

    @Override
    public boolean enablePhotopickerUiCustomizationParamsApi() {
        return getValue(Flags.FLAG_ENABLE_PHOTOPICKER_UI_CUSTOMIZATION_PARAMS_API,
            FeatureFlags::enablePhotopickerUiCustomizationParamsApi);
    }

    @Override
    public boolean enablePickerHighlightSearchResultsApis() {
        return getValue(Flags.FLAG_ENABLE_PICKER_HIGHLIGHT_SEARCH_RESULTS_APIS,
            FeatureFlags::enablePickerHighlightSearchResultsApis);
    }

    @Override
    public boolean enablePickerLocationMetadataApi() {
        return getValue(Flags.FLAG_ENABLE_PICKER_LOCATION_METADATA_API,
            FeatureFlags::enablePickerLocationMetadataApi);
    }

    @Override
    public boolean enableSpecialFormatColumn() {
        return getValue(Flags.FLAG_ENABLE_SPECIAL_FORMAT_COLUMN,
            FeatureFlags::enableSpecialFormatColumn);
    }

    @Override
    public boolean enableStableUrisForExternalPrimaryVolume() {
        return getValue(Flags.FLAG_ENABLE_STABLE_URIS_FOR_EXTERNAL_PRIMARY_VOLUME,
            FeatureFlags::enableStableUrisForExternalPrimaryVolume);
    }

    @Override
    public boolean enableStableUrisForPublicVolume() {
        return getValue(Flags.FLAG_ENABLE_STABLE_URIS_FOR_PUBLIC_VOLUME,
            FeatureFlags::enableStableUrisForPublicVolume);
    }

    @Override
    public boolean enableTrashAndRestoreByFilePathApi() {
        return getValue(Flags.FLAG_ENABLE_TRASH_AND_RESTORE_BY_FILE_PATH_API,
            FeatureFlags::enableTrashAndRestoreByFilePathApi);
    }

    @Override
    public boolean enableUnicodeCheck() {
        return getValue(Flags.FLAG_ENABLE_UNICODE_CHECK,
            FeatureFlags::enableUnicodeCheck);
    }

    @Override
    public boolean excludeUnreliableVolumes() {
        return getValue(Flags.FLAG_EXCLUDE_UNRELIABLE_VOLUMES,
            FeatureFlags::excludeUnreliableVolumes);
    }

    @Override
    public boolean indexMediaLatitudeLongitude() {
        return getValue(Flags.FLAG_INDEX_MEDIA_LATITUDE_LONGITUDE,
            FeatureFlags::indexMediaLatitudeLongitude);
    }

    @Override
    public boolean inferredMediaDate() {
        return getValue(Flags.FLAG_INFERRED_MEDIA_DATE,
            FeatureFlags::inferredMediaDate);
    }

    @Override
    public boolean mediaCognitionService() {
        return getValue(Flags.FLAG_MEDIA_COGNITION_SERVICE,
            FeatureFlags::mediaCognitionService);
    }

    @Override
    public boolean mediaStoreOpenFile() {
        return getValue(Flags.FLAG_MEDIA_STORE_OPEN_FILE,
            FeatureFlags::mediaStoreOpenFile);
    }

    @Override
    public boolean motionPhotoIntent() {
        return getValue(Flags.FLAG_MOTION_PHOTO_INTENT,
            FeatureFlags::motionPhotoIntent);
    }

    @Override
    public boolean pickOrderedImages() {
        return getValue(Flags.FLAG_PICK_ORDERED_IMAGES,
            FeatureFlags::pickOrderedImages);
    }

    @Override
    public boolean pickerAccentColor() {
        return getValue(Flags.FLAG_PICKER_ACCENT_COLOR,
            FeatureFlags::pickerAccentColor);
    }

    @Override
    public boolean pickerDefaultTab() {
        return getValue(Flags.FLAG_PICKER_DEFAULT_TAB,
            FeatureFlags::pickerDefaultTab);
    }

    @Override
    public boolean pickerPreSelectionExtra() {
        return getValue(Flags.FLAG_PICKER_PRE_SELECTION_EXTRA,
            FeatureFlags::pickerPreSelectionExtra);
    }

    @Override
    public boolean pickerRecentSelection() {
        return getValue(Flags.FLAG_PICKER_RECENT_SELECTION,
            FeatureFlags::pickerRecentSelection);
    }

    @Override
    public boolean revokeAccessOwnedPhotos() {
        return getValue(Flags.FLAG_REVOKE_ACCESS_OWNED_PHOTOS,
            FeatureFlags::revokeAccessOwnedPhotos);
    }

    @Override
    public boolean versionLockdown() {
        return getValue(Flags.FLAG_VERSION_LOCKDOWN,
            FeatureFlags::versionLockdown);
    }


    protected boolean getValue(String flagName, Predicate<FeatureFlags> getter) {
        return mGetValueImpl.test(flagName, getter);
    }

    public List<String> getFlagNames() {
        return Arrays.asList(
            Flags.FLAG_ACCESS_MEDIA_OWNER_PACKAGE_NAME_PERMISSION,
            Flags.FLAG_AUDIO_SAMPLE_COLUMNS,
            Flags.FLAG_CLOUD_MEDIA_PROVIDER_SEARCH,
            Flags.FLAG_CLOUD_PROVIDER_ALBUMS_AS_CATEGORY_API,
            Flags.FLAG_ENABLE_BACKUP_AND_RESTORE,
            Flags.FLAG_ENABLE_CLOUD_MEDIA_PROVIDER_CAPABILITIES,
            Flags.FLAG_ENABLE_EMBEDDED_PHOTOPICKER,
            Flags.FLAG_ENABLE_EMBEDDED_PICKER_EXPANDED_HIGHLIGHT_TYPE_API,
            Flags.FLAG_ENABLE_EXCLUSION_LIST_FOR_DEFAULT_FOLDERS,
            Flags.FLAG_ENABLE_LOCAL_MEDIA_PROVIDER_CAPABILITIES,
            Flags.FLAG_ENABLE_MARK_IS_FAVORITE_STATUS_API,
            Flags.FLAG_ENABLE_MARK_MEDIA_AS_FAVORITE_API,
            Flags.FLAG_ENABLE_MEDIA_PROCESSING_SERVICE,
            Flags.FLAG_ENABLE_MEDIA_SEARCH,
            Flags.FLAG_ENABLE_MIME_TYPE_FIX_FOR_ANDROID_15,
            Flags.FLAG_ENABLE_MODERN_PHOTOPICKER,
            Flags.FLAG_ENABLE_OEM_METADATA,
            Flags.FLAG_ENABLE_OEM_METADATA_UPDATE,
            Flags.FLAG_ENABLE_OEM_METADATA_USING_MIMETYPE,
            Flags.FLAG_ENABLE_PHOTOPICKER_DATESCRUBBER,
            Flags.FLAG_ENABLE_PHOTOPICKER_SEARCH,
            Flags.FLAG_ENABLE_PHOTOPICKER_SELECTION_PARAMS_API,
            Flags.FLAG_ENABLE_PHOTOPICKER_TRANSCODING,
            Flags.FLAG_ENABLE_PHOTOPICKER_UI_CUSTOMIZATION_PARAMS_API,
            Flags.FLAG_ENABLE_PICKER_HIGHLIGHT_SEARCH_RESULTS_APIS,
            Flags.FLAG_ENABLE_PICKER_LOCATION_METADATA_API,
            Flags.FLAG_ENABLE_SPECIAL_FORMAT_COLUMN,
            Flags.FLAG_ENABLE_STABLE_URIS_FOR_EXTERNAL_PRIMARY_VOLUME,
            Flags.FLAG_ENABLE_STABLE_URIS_FOR_PUBLIC_VOLUME,
            Flags.FLAG_ENABLE_TRASH_AND_RESTORE_BY_FILE_PATH_API,
            Flags.FLAG_ENABLE_UNICODE_CHECK,
            Flags.FLAG_EXCLUDE_UNRELIABLE_VOLUMES,
            Flags.FLAG_INDEX_MEDIA_LATITUDE_LONGITUDE,
            Flags.FLAG_INFERRED_MEDIA_DATE,
            Flags.FLAG_MEDIA_COGNITION_SERVICE,
            Flags.FLAG_MEDIA_STORE_OPEN_FILE,
            Flags.FLAG_MOTION_PHOTO_INTENT,
            Flags.FLAG_PICK_ORDERED_IMAGES,
            Flags.FLAG_PICKER_ACCENT_COLOR,
            Flags.FLAG_PICKER_DEFAULT_TAB,
            Flags.FLAG_PICKER_PRE_SELECTION_EXTRA,
            Flags.FLAG_PICKER_RECENT_SELECTION,
            Flags.FLAG_REVOKE_ACCESS_OWNED_PHOTOS,
            Flags.FLAG_VERSION_LOCKDOWN
        );
    }

    private Set<String> mReadOnlyFlagsSet = new HashSet<>(
        Arrays.asList(
            ""
        )
    );

    private Map<String, Boolean> mFinalizedFlags = new HashMap<>(
        Map.ofEntries(
            Map.entry(Flags.FLAG_ACCESS_MEDIA_OWNER_PACKAGE_NAME_PERMISSION, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_AUDIO_SAMPLE_COLUMNS, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_CLOUD_MEDIA_PROVIDER_SEARCH, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_CLOUD_PROVIDER_ALBUMS_AS_CATEGORY_API, Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_CLOUD_MEDIA_PROVIDER_CAPABILITIES, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_EMBEDDED_PHOTOPICKER, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_EMBEDDED_PICKER_EXPANDED_HIGHLIGHT_TYPE_API, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_MARK_IS_FAVORITE_STATUS_API, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_MEDIA_PROCESSING_SERVICE, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_MEDIA_SEARCH, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_OEM_METADATA, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_OEM_METADATA_UPDATE, Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_OEM_METADATA_USING_MIMETYPE, Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_PHOTOPICKER_SEARCH, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_PHOTOPICKER_SELECTION_PARAMS_API, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_PHOTOPICKER_TRANSCODING, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_PHOTOPICKER_UI_CUSTOMIZATION_PARAMS_API, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_PICKER_HIGHLIGHT_SEARCH_RESULTS_APIS, Build.VERSION.SDK_INT >= 36 && Build.VERSION.SDK_INT_FULL >= 3600001 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_SPECIAL_FORMAT_COLUMN, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_STABLE_URIS_FOR_EXTERNAL_PRIMARY_VOLUME, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_TRASH_AND_RESTORE_BY_FILE_PATH_API, Build.VERSION.SDK_INT >= 37 ? true : false),
            Map.entry(Flags.FLAG_ENABLE_UNICODE_CHECK, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_INFERRED_MEDIA_DATE, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_MEDIA_COGNITION_SERVICE, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_MEDIA_STORE_OPEN_FILE, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_MOTION_PHOTO_INTENT, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_PICK_ORDERED_IMAGES, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_PICKER_ACCENT_COLOR, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_PICKER_DEFAULT_TAB, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_PICKER_PRE_SELECTION_EXTRA, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_PICKER_RECENT_SELECTION, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_REVOKE_ACCESS_OWNED_PHOTOS, Build.VERSION.SDK_INT >= 36 ? true : false),
            Map.entry(Flags.FLAG_VERSION_LOCKDOWN, Build.VERSION.SDK_INT >= 36 ? true : false),
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
