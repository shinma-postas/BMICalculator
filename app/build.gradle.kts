plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bmicalculator"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    // viewBinding有効化
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.bmicalculator"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // viewModels()のproperty delegateの依存関係
    implementation("androidx.fragment:fragment-ktx:1.8.9")

    // dataStoreの依存関係
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

}
