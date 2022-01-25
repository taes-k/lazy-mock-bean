dependencies {
    implementation("org.springframework:spring-context:5.3.15")
    implementation("org.springframework:spring-test:5.3.15")
    implementation("org.mockito:mockito-all:1.10.19")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = rootProject.name
            from(components["java"])
        }
    }
}
