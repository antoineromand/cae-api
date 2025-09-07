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
    testImplementation(project(":migrations"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.redis:testcontainers-redis")


    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.flywaydb:flyway-core:11.10.4")
    testRuntimeOnly("org.flywaydb:flyway-database-postgresql:11.10.4")

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

