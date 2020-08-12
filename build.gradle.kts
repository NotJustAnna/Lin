import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    maven
    `maven-publish`
    //application
    //id("com.github.johnrengelman.shadow") version "6.0.0"
    id("com.jfrog.bintray") version "1.8.5"
    id("org.jetbrains.dokka") version "0.10.1"
}

group = "io.github.cafeteriaguild"
version = "0.1.2"

repositories {
    jcenter()
}

//application.mainClassName = "io.github.cafeteriaguild.lin.LinUtilsKt"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("net.notjustanna:tartar:1.6")
    //implementation("it.unimi.dsi:fastutil:8.3.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

val sourceJar by tasks.creating(Jar::class) {
    dependsOn(tasks["classes"])
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
    //configuration {
    //    perPackageOption {
    //        prefix = "net.notjustanna.tartar.impl"
    //        suppress = true
    //    }
    //}
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(dokka)
    archiveClassifier.set("javadoc")
    from(dokka.outputDirectory)
}

publishing {
    publications.create("mavenJava", MavenPublication::class.java) {
        groupId = project.group.toString()
        artifactId = project.name
        version = project.version.toString()

        from(components["kotlin"])
        artifact(sourceJar)
        artifact(javadocJar)
    }
}

fun findProperty(s: String) = project.findProperty(s) as String?
bintray {
    user = findProperty("bintrayUsername")
    key = findProperty("bintrayApiKey")
    publish = true
    setPublications("mavenJava")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = project.name
        setLicenses("MIT")
        vcsUrl = "https://github.com/CafeteriaGuild/Lin.git"
    })
}

tasks.withType<BintrayUploadTask> {
    dependsOn("build", "publishToMavenLocal")
}