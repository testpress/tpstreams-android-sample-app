# TPStreams Integration Guide with ExoPlayer
Welcome to the TPStreams integration guide. This document will help you integrate TPStreams with ExoPlayer in your Android app. You'll learn how to configure ExoPlayer for both DRM and non-DRM content, generate access tokens, and retrieve playback URLs.

## Prerequisites
Before you begin, ensure you have the following:
- An Android project running at least API level 21 (Lollipop).
- [ExoPlayer](https://developer.android.com/media/media3/exoplayer/hello-world) integrated into your project.
- Access to TPStreams API credentials (organization code, asset IDs, etc.).

## Setup and Installation
### Add Required Dependencies
Ensure your build.gradle file includes the necessary dependencies for ExoPlayer and networking libraries:
```
dependencies {
    // ExoPlayer (Media3)
    implementation 'androidx.media3:media3-exoplayer:1.4.1'
    implementation 'androidx.media3:media3-ui:1.4.1'
    implementation 'androidx.media3:media3-common:1.4.1'
    implementation 'androidx.media3:media3-exoplayer-hls:1.4.1'
    implementation 'androidx.media3:media3-exoplayer-dash:1.4.1'
}
```

## Quick Start Guide
### 1. Generate Access Token
Before retrieving the playback URL, you need to generate an access token for the specific asset. Please refer to our [Access Token Generation Documentation](https://developer.tpstreams.com/docs/server-api/access-token) for detailed instructions on how to obtain an access token.

### 2. Retrieve Playback URL
 Please refer to our [Get Individual Asset Details Documentation](https://developer.tpstreams.com/docs/server-api/assets#get-individual-asset-details) for detailed instructions on how to obtain the asset details. 

### 3. Construct DRM License URL 
```
private fun getDRMLicenseUrl(): String {
    val videoId = VIDEO_ID
    val accessToken = getAccessToken()
    return "https://app.tpstreams.com/api/v1/$ORG_CODE/assets/$videoId/drm_license/?access_token=$accessToken"
}
```
 
### 4. Configure and Play with ExoPlayer
For a detailed example of how to configure and play media using ExoPlayer, please refer to the sample app provided in this documentation. The sample app includes comprehensive implementations of ExoPlayer setup, DRM configuration, and playback control.

Check the sample app to see how to:
- Set up ExoPlayer with the required media source and DRM configuration.
- Handle playback state changes and errors.

You can find the sample app at the following link: [TPStreams Sample App](https://github.com/testpress/tpstreams-android-sample-app).
