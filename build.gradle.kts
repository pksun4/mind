import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.allopen") version "1.9.0"
    kotlin("plugin.noarg") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    kotlin("kapt") version "1.9.0"
    id("org.springframework.boot") version "3.2.11"
    id("io.spring.dependency-management") version "1.1.6"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val springBootVersion: String by project
val springDataCommons: String by project
val queryDsl: String by project

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

        // JPA QueryDSL
        implementation("org.springframework.data:spring-data-commons:$springDataCommons")
        implementation("com.querydsl:querydsl-jpa:$queryDsl:jakarta")
        implementation("com.querydsl:querydsl-apt:$queryDsl:jakarta")

        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

project(":core") {
    apply {
        plugin("kotlin-allopen")
        plugin("kotlin-noarg")
        plugin("kotlin-kapt")
    }

    noArg {
        annotation("jakarta.persistence.Entity")
    }

    allOpen {
        annotation("jakarta.persistence.Entity")
        annotation("jakarta.persistence.MappedSuperclass")
        annotation("jakarta.persistence.Embeddable")
    }

    dependencies {
        compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
        compileOnly("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
        kapt("jakarta.persistence:jakarta.persistence-api")
        kapt("jakarta.annotation:jakarta.annotation-api")

        // Hikari
        implementation("com.zaxxer:HikariCP:5.0.1")
        // MySql
        runtimeOnly("com.mysql:mysql-connector-j")
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }

    tasks.getByName<BootJar>("bootJar") {
        enabled = false
    }
}

project(":api") {
    apply {
        plugin("kotlin-kapt")
    }

    dependencies {
        implementation(project(":core"))
        implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
        implementation("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
        kapt("jakarta.persistence:jakarta.persistence-api")
        kapt("jakarta.annotation:jakarta.annotation-api")
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

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}
