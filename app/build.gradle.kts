plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")
    id ("androidx.navigation.safeargs.kotlin")
}



android {
    namespace = "com.example.a_connect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.a_connect"
        minSdk = 26
        targetSdk = 35
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
        buildConfig = true
        viewBinding = true
    }
    dataBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.gridlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.imageslideshow)


    //Firebase

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    //For Circular Image
    implementation(libs.circleimageview)


// Apache POI for reading Excel files
    implementation(libs.poi)
    implementation (libs.poi.ooxml)

    implementation(libs.glide)
    annotationProcessor(libs.compiler)
   // ksp("com.github.bumptech.glide:compiler:4.12.0") // Glide compiler


    //Lottie Animation
    implementation (libs.lottie)
    implementation (libs.firebase.firestore.ktx)



// Add the dependency for the Vertex AI in Firebase library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.vertexai)

    implementation (libs.androidx.paging.runtime.ktx)


}

