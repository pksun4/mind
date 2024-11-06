import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.11"
    id("io.spring.dependency-management") version "1.1.6"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val springBootVersion: String by project

allprojects {
    group = "com.mind"
    version = "1.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.set(listOf("-Xjsr305=strict"))
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("kotlin")
        plugin("kotlin-spring")
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
        implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

project(":core") {
    dependencies {

    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }

    tasks.getByName<BootJar>("bootJar") {
        enabled = false
    }
}

project(":api") {
    dependencies {
        implementation(project(":core"))
    }

    val moduleMainClass = "com.mind.api.ApiApplicationKt"
    tasks.getByName<BootJar>("bootJar") {
        enabled = true
        mainClass.set(moduleMainClass)
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}
