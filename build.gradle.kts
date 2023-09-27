plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20230227")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    testImplementation("junit:junit:4.13.1")
}

group = "one.tain"
version = "5.1.1"
description = "emoji"
java.sourceCompatibility = JavaVersion.VERSION_1_8

