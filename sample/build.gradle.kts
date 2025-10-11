plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

android {
    compileSdk = (project.property("compilesdk_version") as String).toInt()
    namespace = "com.github.muhrifqii.parserss.sample"
    //noinspection GradleDependency
    defaultConfig {
        applicationId = "com.github.muhrifqii.parserss.sample"
        minSdk = 21
        versionCode = (project.property("project_version_code") as String).toInt()
        versionName = project.property("project_version") as String
        multiDexEnabled = true
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
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.constraintlayout)
    implementation(libs.material)

    implementation(libs.fuel)

    implementation(libs.multidex)

    implementation(project(":parserss"))
    implementation(project(":fuel"))
    implementation(project(":retrofit"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.espresso.core)
}
