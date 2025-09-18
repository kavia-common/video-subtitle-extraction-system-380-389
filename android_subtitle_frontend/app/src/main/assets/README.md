# App Assets

This folder is used to store non-compiled assets that are packaged with the Android app (e.g., small media samples, models, etc.).

Sample video (local testing):
- File name: sample.mp4
- Location: app/src/main/assets/sample.mp4
- Purpose: Allow the app to play a local, bundled video for testing the player and subtitle overlay without needing external storage or internet.

Instructions to add:
1) Place a small, license-free MP4 file here and name it exactly: sample.mp4
   - Recommended: keep the file small (<2 MB) to avoid bloating the APK.
   - Example sources for tiny sample videos (license-free): 
     - https://sample-videos.com (choose a very small MP4)
     - https://filesamples.com
2) After placing the file, rebuild the app. Android will package it into the APK under the assets folder.
3) Access in code via AssetManager:
   - val assetFileDescriptor = context.assets.openFd("sample.mp4")
   - Or if using ExoPlayer, you can use an AssetDataSource with the URI: "asset:///sample.mp4"

Note:
- This repository does not commit binary media files to keep the repo size small and avoid licensing issues.
- If a model.tflite is later added, also place it here and reference it from assets.

Verification:
- After adding sample.mp4, run the app and load the video using an asset scheme (e.g., "asset:///sample.mp4") or expose a UI option to play the local asset.
