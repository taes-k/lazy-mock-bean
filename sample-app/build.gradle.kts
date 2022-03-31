plugins {
    id("org.springframework.boot") version "2.6.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

//    testImplementation("com.github.taes-k:lazy-mock-bean:$version")
    testImplementation(project(":app"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

tasks.withType<PublishToMavenRepository> {
    enabled = false
}
