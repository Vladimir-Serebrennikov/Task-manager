plugins {
    application
    id("java")
    id("jacoco")
    //id("checkstyle")
    id("io.freefair.lombok") version "8.4"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.github.ben-manes.versions") version "0.50.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.sentry.jvm.gradle") version "4.2.0"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
application { mainClass.set("hexlet.code.AppApplication") }

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.instancio:instancio-junit:3.6.0")
    implementation("net.datafaker:datafaker:2.0.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("io.sentry:sentry-spring-boot-starter:7.3.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    runtimeOnly("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.6.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

if (System.getenv("APP_ENV") == "production") {
    sentry {
        includeSourceContext = true
        org = "vladimir-serebrennikov"
        projectName = "java-spring-boot"
        authToken = System.getenv("SENTRY_AUTH_TOKEN")
    }
}