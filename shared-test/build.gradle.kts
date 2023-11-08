plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.trachlai.shared_test"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
    }
}

dependencies {
    implementation(project(":app"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("junit:junit:4.13.2")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.44.2")
    implementation("com.google.dagger:hilt-android-testing:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")
    //KSP
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")
    //Room
    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

}