# Ocean Subtitles (Android)

This app demonstrates:
- Selecting a local MP4 using the Storage Access Framework (SAF).
- Playing the selected video with ExoPlayer.
- Extracting subtitles via an integrated ML interface (stubbed, TFLite-ready) and displaying them beneath the player.
- Ocean Professional theme: modern, minimalist look with blue and amber accents and rounded corners.

How to use:
1) Build and run the app on a device/emulator.
2) Tap "Open Video" and pick a local MP4 from device storage.
3) The video will start playing; the subtitle overlay will update periodically.
   - The current ML is a stub; it simulates text output. Replace with a real TFLite model for production.

Optional: Bundled sample video
- Place a tiny file named "sample.mp4" into app/src/main/assets/.
- Rebuild the app. You can then load it with URI "asset:///sample.mp4" (add a menu/button if desired).

Integrating a real TFLite model
- Put model.tflite under app/src/main/assets/.
- Create an implementation of `SubtitleExtractor` that loads the model and runs inference on captured frames.
- Wire it in `PlayerViewModel` by replacing `TFLiteSubtitleExtractorStub` with your real implementation.

Notes
- On Android 13+ (API 33), READ_EXTERNAL_STORAGE permission is not required for SAF.
- Theme files are in res/values and res/drawable and follow the Ocean Professional style guide.
