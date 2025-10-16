import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.library)
    kotlin("multiplatform")
    `maven-publish`
    signing
}

group = "com.github.muhrifqii.ParseRSS"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

android {
    compileSdk = (project.property("compilesdk_version") as String).toInt()
    namespace = "com.github.muhrifqii.ParseRSS"
    defaultConfig {
        minSdk = (project.property("minsdk_version") as String).toInt()
    }
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm()
    androidTarget()
    iosSimulatorArm64()
    targets.all {
        if (this is KotlinJvmTarget) {
            testRuns.all {
                executionTask.configure {
                    useJUnitPlatform()
                }
            }
        }
        @Suppress("OPT_IN_USAGE")
        if (this is HasConfigurableKotlinCompilerOptions<*>) {
            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
        }
        val jvmCommon by creating {
            dependsOn(commonMain)
        }
        androidMain {
            dependsOn(jvmCommon)
        }
        jvmMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.kxml)
            }
        }
        nativeMain {
            dependencies {
                implementation(libs.ktxml)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val jvmCommonTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.junit)
                implementation(libs.mockito.core)
                runtimeOnly(libs.junit.engine)
            }
        }
        jvmTest {
            dependsOn(jvmCommonTest)
        }
        androidUnitTest {
            dependsOn(jvmCommonTest)
            dependencies {
                implementation(libs.robolectric)
                implementation(libs.truth)
            }
        }
        nativeTest {
        }
    }
}
