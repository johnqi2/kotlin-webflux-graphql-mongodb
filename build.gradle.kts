import com.adarshr.gradle.testlogger.theme.ThemeType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  val kotlinVersion = "1.2.71"

  application
  kotlin("jvm") version kotlinVersion
  id("org.springframework.boot") version "2.1.2.RELEASE"
  id("io.spring.dependency-management") version "1.0.6.RELEASE"
  id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
  id("com.adarshr.test-logger") version "1.6.0"
}

repositories {
  jcenter()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.graphql-java:graphql-java:11.0")
  implementation("com.graphql-java:graphql-java-spring-boot-starter-webflux:1.0")
  implementation("org.springframework.boot:spring-boot-starter-actuator")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))
  // force explicit dependency
  implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
  // logging
  implementation("io.github.microutils:kotlin-logging:1.6.22")

  testImplementation("io.projectreactor:reactor-test")
  // use junit 5
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "junit", module = "junit")
  }
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.2.0")
}

application {
  group = "com.example.ktboot"
  version = "0.5.2"
  mainClassName = "com.example.ktboot.ApplicationKt"
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

springBoot {
  buildInfo()
}

tasks {
  withType(KotlinCompile::class).configureEach {
    kotlinOptions {
      jvmTarget = JavaVersion.VERSION_1_8.toString()
      freeCompilerArgs = listOf("Xjsr305=strict")
    }
  }

  testlogger {
    theme = ThemeType.MOCHA_PARALLEL
  }

  withType(Test::class).configureEach {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    useJUnitPlatform()
    reports.html.isEnabled = false
    reports.junitXml.isEnabled = false
  }
}

