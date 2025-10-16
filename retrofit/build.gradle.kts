plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

//apply(from = "../reportings.gradle")

group = "com.github.muhrifqii.ParseRSS"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

android {
    compileSdk = (project.property("compilesdk_version") as String).toInt()
    namespace = "com.github.muhrifqii.ParseRSS"
    //noinspection GradleDependency
    defaultConfig {
        minSdk = (project.property("minsdk_version") as String).toInt()
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":parserss"))

    implementation(libs.retrofit)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.core)
}
