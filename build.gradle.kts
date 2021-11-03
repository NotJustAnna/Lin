plugins {
    kotlin("multiplatform") version "1.5.31"
}

group = "net.notjustanna"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.cafeteria.dev") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "13"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        browser()
        nodejs()
    }

    linuxX64("linuxX64")
    macosX64("macosX64")
    // macosArm64("macosArm64") // TODO
    mingwX64("mingwX64")

    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }
        val commonMain by getting {
            dependencies {
                implementation("net.notjustanna:tartar:2.3")
                implementation("com.squareup.okio:okio:3.0.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("net.notjustanna:kotlin-unified-platform:1.2")
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val nativeTest by creating {
            dependsOn(nativeMain)
        }
        val linuxX64Main by getting {
            dependsOn(nativeMain)
        }
        val linuxX64Test by getting {
            dependsOn(nativeTest)
        }
        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }
        val mingwX64Test by getting {
            dependsOn(nativeTest)
        }
        val macosX64Main by getting {
            dependsOn(nativeMain)
        }
        val macosX64Test by getting {
            dependsOn(nativeTest)
        }
    }
}
