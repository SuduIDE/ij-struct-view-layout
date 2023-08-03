plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.8.21"
  id("org.jetbrains.intellij") version "1.14.1"
  kotlin("plugin.serialization") version "1.8.21"
}

group = "com.intership"
version = "0.2"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation(kotlin("test"))
  testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
    because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
  }
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
  implementation("com.squareup.moshi:moshi:1.14.0")
  implementation("com.squareup.moshi:moshi-adapters:1.14.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2023.2")
  type.set("IC") // Target IDE Platform
  plugins.set(listOf("com.intellij.java"))
}

sourceSets {
  main {
    java {
      srcDirs("src/main/kotlin/")
    }
  }

  test {
    java {
      srcDirs("src/test/kotlin/")
    }
  }
}

tasks {
  test {
    useJUnitPlatform()
  }
  // Set the JVM compatibility versions
  compileKotlin {
    kotlinOptions.jvmTarget = "17"
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = "17"
  }

  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }

  patchPluginXml {
    sinceBuild.set("231")
    untilBuild.set("232.*")
  }

  buildSearchableOptions {
    enabled = false
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}
