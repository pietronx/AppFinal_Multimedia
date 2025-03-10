plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.galleta"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.galleta"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // APIs
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")

    //Gif
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.23")

    //Gemini API
//    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
//    implementation("com.google.guava:guava:31.0.1-android")
//    implementation("org.reactivestreams:reactive-streams:1.0.4")

}