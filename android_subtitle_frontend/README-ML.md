# Ocean Subtitles — On-device ML Subtitle Extraction (Stub)

This app includes a stubbed on-device subtitle extractor designed to be replaced with a real TensorFlow Lite pipeline.

Current implementation:
- app/src/main/kotlin/org/example/app/ml/SubtitleExtractor.kt
- `TFLiteSubtitleExtractorStub` simulates inference and returns canned phrases based on playback time.

How to integrate a real TFLite model:
1. Place your model in `app/src/main/assets/model.tflite`.
2. Replace `TFLiteSubtitleExtractorStub` with a real implementation that:
   - Loads the model:
     ```kotlin
     val modelBuffer = FileUtil.loadMappedFile(context, "model.tflite")
     val interpreter = Interpreter(modelBuffer, Interpreter.Options())
     ```
   - Preprocess input frames (PCM audio or video frames) into tensors.
   - Run inference and postprocess output to text.
3. Obtain audio/video frames:
   - For audio-based ASR: tap into the audio stream of the player or decode using AudioRecord (microphone) for live sources.
   - For video frames: use ExoPlayer's video frame APIs or MediaCodec/MediaExtractor pipeline.
4. Update `PlayerViewModel` to feed real frames to the extractor.
5. Keep inference on a background thread to avoid UI jank.

Note:
- This project uses ExoPlayer and LiveData for state management.
- Styling follows the Ocean Professional theme with blue (#2563EB) and amber (#F59E0B) accents.
