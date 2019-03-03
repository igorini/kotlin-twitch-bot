import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val kotlinVersion by extra { "1.3.21" }

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
    }
}

val kotlinVersion: String by extra
val koinVersion = "0.9.0"
val kotlinLoggingVersion = "1.5.3"
val twitch4jVersion = "v0.10.1"
val jacksonVersion = "2.9.8"
val caffeineVersion = "2.6.2"
val jacksonXmlDatabindVersion = "0.6.2"

plugins {
    idea
    kotlin("jvm") version "1.3.21"
    `maven-publish`
}

group = "com.igorini"
version = "0.0.11"

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    jcenter()
    mavenCentral()
}

configure<IdeaModel> {
    project {
        languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_1_8)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

dependencies {
    // Kotlin
    compile(kotlin("stdlib", kotlinVersion))
    compile(kotlin("reflect", kotlinVersion))

    // Kotlin Logging
    compile("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")

    // Koin
    compile("org.koin:koin-core:$koinVersion")

    // Twitch4j
    compile("com.github.twitch4j:twitch4j:$twitch4jVersion")

    // Jackson
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    //compile("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")

    // Caffeine (caching library)
    compile("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}