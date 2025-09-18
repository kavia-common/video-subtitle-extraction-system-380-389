androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
        implementation("androidx.activity:activity-ktx:1.9.2")
        implementation("com.google.android.exoplayer:exoplayer:2.19.1")
        implementation("org.tensorflow:tensorflow-lite:2.14.0")
        implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    }
}
