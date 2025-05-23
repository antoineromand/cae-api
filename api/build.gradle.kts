import java.util.Properties


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



dependencies {
	implementation(project(":authentication"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.modulith:spring-modulith-starter-core")
	implementation("org.springframework.modulith:spring-modulith-starter-jpa")
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
