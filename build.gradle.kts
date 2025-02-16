import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "com.dodo"
version = "0.0.1-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.github.microutils:kotlin-logging:3.0.5")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.telegram:telegrambots-spring-boot-starter:6.8.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.strikt:strikt-core:0.32.0")
	testImplementation("io.mockk:mockk:1.12.0")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootJar> {
    mainClass.set("com.dodo.fplbot.FplbotApplicationKt")
}

kotlin {
	jvmToolchain(21)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
        freeCompilerArgs += "-Xjsr305=strict"
    }
}
