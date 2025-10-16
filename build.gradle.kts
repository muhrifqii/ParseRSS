plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.dokka) apply false
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/detekt.yml")
    parallel = true

    source.setFrom(projectDir)
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(false)
        sarif.required.set(true)
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

tasks.register("testAll") {
    group = "verification"
    description = "Run tests for all platforms"

    dependsOn(
        "jvmTest",
        "testDebugUnitTest",
        "iosSimulatorArm64Test"
    )
}
