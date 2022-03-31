dependencies {
    implementation("org.springframework:spring-context:5.3.15")
    implementation ("org.springframework:spring-test:5.3.15")
    compileOnly ("org.projectlombok:lombok:1.18.22")
    annotationProcessor ("org.projectlombok:lombok:1.18.22")
    implementation ("org.apache.commons:commons-lang3:3.12.0")
    implementation ("org.mockito:mockito-all:1.10.19")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = rootProject.name
            from(components["java"])
        }
    }
}