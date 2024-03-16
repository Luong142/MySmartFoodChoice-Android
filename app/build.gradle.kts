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
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    implementation ("com.google.mlkit:image-labeling-custom:17.0.2")
    implementation ("com.google.android.gms:play-services-mlkit-image-labeling-custom:16.0.0-beta5")
    // TODO: this one is for API calling, middle man
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.android.libraries.healthdata:health-data-api:1.0.0-alpha01")

    // for chart
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation ("com.nineoldandroids:library:2.4.0")

    // Image labeling feature with model downloaded from Firebase
    implementation ("com.google.mlkit:image-labeling-custom:17.0.2")
    // Or use the dynamically downloaded pipeline in Google Play Services
    // implementation 'com.google.android.gms:play-services-mlkit-image-labeling-custom:16.0.0-beta5'
    implementation ("com.google.mlkit:linkfirebase:17.0.0")
    // TODO: we need two more APIs like Edamame food api, and Google Cloud Vision api.
    // implementation

    implementation ("com.squareup.picasso:picasso:2.71828")
    // Tensorflow Lite dependencies
    implementation("androidx.activity:activity-ktx:1.8.2")
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
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
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