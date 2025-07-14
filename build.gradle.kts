plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.plugin)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

allprojects {
    group = "com.github.keunwon"
    version = "1.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    dependencies {
        implementation(rootProject.libs.kotlin.reflection)
        implementation(rootProject.libs.jackson.module.kotlin)

        testImplementation(rootProject.libs.kotest.assertions.core)
        testImplementation(rootProject.libs.kotest.extensions.spring)
        testImplementation(rootProject.libs.kotest.runner.junit5)
        testImplementation(rootProject.libs.kotlin.test.junit5)
        testImplementation(rootProject.libs.mockk)
        testImplementation(rootProject.libs.spring.boot.starter.security.test)
        testImplementation(rootProject.libs.spring.boot.starter.test)
        testRuntimeOnly(rootProject.libs.junit.platform.launcher)

        annotationProcessor(rootProject.libs.spring.boot.configuration.processor)
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    dependencyManagement {
        extra["springCloudVersion"] = "2025.0.0"
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        jvmArgs(
            "-Xshare:off"
        )
    }
}
