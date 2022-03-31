plugins {
    java
    `maven-publish`
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    group = "io.github.lazymockbean"
    version = "0.0.8-m6"
}

subprojects {
    apply {
        plugin("java")
        plugin("maven-publish")
    }
}
