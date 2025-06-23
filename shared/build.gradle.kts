plugins {
	java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

dependencies {
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.register<Test>("unitTest") {
    useJUnitPlatform {
        includeTags("unit")
    }
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