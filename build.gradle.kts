plugins {
    kotlin("multiplatform") version "1.5.31"
}

group = "com.github.adriantodt"
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
    js(BOTH) {
        browser {

        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.github.adriantodt:tartar:2.2")
                implementation("com.squareup.okio:okio:3.0.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val commonNonJvmMain by creating {
            dependsOn(commonMain)
        }
        val jsMain by getting {
            dependsOn(commonNonJvmMain)
        }
        val jsTest by getting
        val nativeMain by getting {
            dependsOn(commonNonJvmMain)
        }
        val nativeTest by getting
    }
}
