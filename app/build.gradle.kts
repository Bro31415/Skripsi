plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.skripsi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.skripsi"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity:1.10.0")
    implementation("androidx.test.services:storage:1.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.1"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")


    implementation("io.ktor:ktor-client-android:3.0.3") // http client & server framework untuk kotlin, intinya backbone untuk ngehubungin supabase sama project kotlin ini
    implementation("io.ktor:ktor-client-cio:3.0.3") // handle http network request
    implementation("io.ktor:ktor-client-content-negotiation:3.0.3") // serialize/deserialize json responses -> untuk parsing

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1") // handle coroutines di app

    implementation("com.ToxicBakery.library.bcrypt:bcrypt:+")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.material:material-icons-extended")


    implementation("androidx.preference:preference-ktx:1.2.1")

    // jetpack compose
    implementation ("androidx.activity:activity-compose:1.10.1")
    implementation ("androidx.compose.foundation:foundation:1.7.8")
    implementation ("androidx.compose.material3:material3:1.3.2")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.4")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Supabase Storage
    implementation("io.github.jan-tennert.supabase:storage-kt")

    // Image Picker
    implementation ("com.github.dhaval2404:imagepicker:2.1")



    // Image Loader (Glide)
    implementation("com.github.bumptech.glide:glide:4.16.0")
//    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Permission Handling
    implementation("com.guolindev.permissionx:permissionx:1.7.1")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.0")

    implementation ("androidx.preference:preference:1.2.1")
}