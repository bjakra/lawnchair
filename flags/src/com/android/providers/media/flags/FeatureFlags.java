package com.android.providers.media.flags;

/** @hide */
public interface FeatureFlags {

    boolean accessMediaOwnerPackageNamePermission();

    boolean audioSampleColumns();

    boolean cloudMediaProviderSearch();

    boolean cloudProviderAlbumsAsCategoryApi();

    boolean enableBackupAndRestore();

    boolean enableCloudMediaProviderCapabilities();

    boolean enableEmbeddedPhotopicker();

    boolean enableEmbeddedPickerExpandedHighlightTypeApi();

    boolean enableExclusionListForDefaultFolders();

    boolean enableLocalMediaProviderCapabilities();

    boolean enableMarkIsFavoriteStatusApi();

    boolean enableMarkMediaAsFavoriteApi();

    boolean enableMediaProcessingService();

    boolean enableMediaSearch();

    boolean enableMimeTypeFixForAndroid15();

    boolean enableModernPhotopicker();

    boolean enableOemMetadata();

    boolean enableOemMetadataUpdate();

    boolean enableOemMetadataUsingMimetype();

    boolean enablePhotopickerDatescrubber();

    boolean enablePhotopickerSearch();

    boolean enablePhotopickerSelectionParamsApi();

    boolean enablePhotopickerTranscoding();

    boolean enablePhotopickerUiCustomizationParamsApi();

    boolean enablePickerHighlightSearchResultsApis();

    boolean enablePickerLocationMetadataApi();

    boolean enableSpecialFormatColumn();

    boolean enableStableUrisForExternalPrimaryVolume();

    boolean enableStableUrisForPublicVolume();

    boolean enableTrashAndRestoreByFilePathApi();

    boolean enableUnicodeCheck();

    boolean excludeUnreliableVolumes();

    boolean indexMediaLatitudeLongitude();

    boolean inferredMediaDate();

    boolean mediaCognitionService();

    boolean mediaStoreOpenFile();

    boolean motionPhotoIntent();

    boolean pickOrderedImages();

    boolean pickerAccentColor();

    boolean pickerDefaultTab();

    boolean pickerPreSelectionExtra();

    boolean pickerRecentSelection();

    boolean revokeAccessOwnedPhotos();

    boolean versionLockdown();

}
