plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

dependencies {
    api(project(":shared"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	compileOnly("org.projectlombok:lombok")
//	annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.register<Test>("integrationTest") {
    useJUnitPlatform {
        includeTags("integration")
    }
}

tasks.register<Test>("functionalTest") {
    useJUnitPlatform {
        includeTags("functional")
    }
}

tasks.named<Test>("test") {
    onlyIf {
        val requested = gradle.startParameter.taskNames
        !requested.contains("unitTest") &&
                !requested.contains("integrationTest") &&
                !requested.contains("functionalTest")
    }
}

