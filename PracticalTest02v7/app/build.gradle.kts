plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "ro.pub.cs.systems.eim.practicaltest02v7"
    compileSdk = 34

    defaultConfig {
        applicationId = "ro.pub.cs.systems.eim.practicaltest02v7"
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
    implementation(files("..\\httpclient-4.4.1.2\\httpclient-4.4.1.2.jar"))
    implementation(files("..\\jsoup-1.10.2\\jsoup-1.10.2.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}