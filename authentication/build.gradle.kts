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
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.security:spring-security-crypto")
//	compileOnly("org.projectlombok:lombok")
//	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
	runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
	onlyIf {
		val requested = gradle.startParameter.taskNames
		requested.none { it.contains("unitTest") || it.contains("integrationTest") || it.contains("functionalTest") }
	}
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