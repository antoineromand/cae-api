import java.util.*


plugins {
	java
	id("org.springframework.boot") version "3.4.5"
}

val lombokVersion = "1.18.38"


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

tasks.named<Jar>("bootJar") {
    archiveFileName.set("pickandeat-api-${project.version}.jar")
}


dependencies {
	implementation(project(":authentication"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter")
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set("com.pickandeat.api.PickAndEatApplication")
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    val envFile = rootProject.file(".env")

    if (envFile.exists()) {
        val props = Properties()
        envFile.reader().use { props.load(it) }

        val projectMode = props.getProperty("PROJECT_MODE") ?: "prod"

        if (projectMode == "dev") {
            println("PROJECT_MODE=dev detected, injecting .env variables...")

            props.forEach { key, value ->
                environment(key.toString(), value.toString())
            }
        }
    }
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