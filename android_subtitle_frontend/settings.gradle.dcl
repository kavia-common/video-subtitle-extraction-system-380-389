pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.experimental.android-ecosystem").version("0.1.43")
}

rootProject.name = "ocean-subtitles"

include("app")

defaults {
    androidApplication {
        jdkVersion = 17
        compileSdk = 34
        minSdk = 30

        versionCode = 1
        versionName = "0.1"
        applicationId = "org.gradle.experimental.android.app"
    }
}
