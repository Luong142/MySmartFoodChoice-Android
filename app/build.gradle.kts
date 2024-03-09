plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android") version "1.7.20"
}

android {
    namespace = "com.example.myfoodchoice"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myfoodchoice"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Firebase dependencies.
    implementation ("com.squareup.picasso:picasso:2.71828")
    // Tensorflow Lite dependencies
    implementation ("org.tensorflow:tensorflow-lite-task-vision-play-services:0.4.2")
    implementation ("com.google.android.gms:play-services-tflite-gpu:16.2.0")
    implementation("androidx.activity:activity:1.8.0")
    val camerax_version = "1.3.1"
    //noinspection GradleDependency
    implementation ("androidx.camera:camera-camera2:$camerax_version")
    //noinspection GradleDependency
    implementation ("androidx.camera:camera-lifecycle:$camerax_version")
    //noinspection GradleDependency
    implementation ("androidx.camera:camera-view:$camerax_version")
    //noinspection GradleDependency
    implementation ("androidx.camera:camera-video:$camerax_version")
    //noinspection GradleDependency
    implementation ("androidx.camera:camera-extensions:$camerax_version")
    implementation ("io.github.pilgr:paperdb:2.7.2")
    implementation("com.google.firebase:firebase-storage")
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}