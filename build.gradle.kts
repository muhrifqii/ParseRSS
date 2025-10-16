import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.dokka) apply false
}

tasks.withType<Detekt> {
    buildUponDefaultConfig = true
    config.setFrom("$projectDir/detekt.yml")
    parallel = true
    source(
        "$projectDir/parserss",
        "$projectDir/retrofit",
        "$projectDir/fuel",
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(false)
        sarif.required.set(true)
    }

    include(
        "**/*.kt",
    )
    exclude(
        "**/src/*test/**",
        "**/src/*Test/**",
        "**/*.gradle.kts"
    )

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
