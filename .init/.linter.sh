#!/bin/bash
cd /home/kavia/workspace/code-generation/video-subtitle-extraction-system-380-389/android_subtitle_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

