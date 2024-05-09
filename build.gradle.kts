plugins {
    kotlin("jvm") version "1.9.23"
}

group = "niceland.vpn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation(kotlin("test"))
    val ktor_version= "2.3.10"
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}