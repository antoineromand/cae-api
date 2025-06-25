plugins {
	java
	id("io.spring.dependency-management") version "1.1.7"
	id("co.uzzu.dotenv.gradle") version "4.0.0"

}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

var currentVersion = "0.0.1-SNAPSHOT"

allprojects {
	group = "com.pickandeat"
	version = currentVersion
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin= "jacoco")
	dependencyManagement {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.5")
		}
	}
	dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter")
		testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
		testImplementation("org.testcontainers:testcontainers:1.21.0")
		testImplementation("org.testcontainers:postgresql:1.21.0")
		testImplementation("org.testcontainers:junit-jupiter:1.21.0")
		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
    }
	tasks.withType<Test> {
		useJUnitPlatform()
		finalizedBy(tasks.named("jacocoTestReport"))
	}

	tasks.named("jacocoTestReport") {
        dependsOn(tasks.named("test"))
    }
}

tasks.register("unitTest") {
	group = "verification"
	description = "Run all unit tests across modules"
	dependsOn(
		subprojects.mapNotNull { it.tasks.findByName("unitTest") }
	)
}

tasks.register("integrationTest") {
	group = "verification"
	description = "Run all integration tests across modules"
	dependsOn(
		subprojects.mapNotNull { it.tasks.findByName("integrationTest") }
	)
}

tasks.register("functionalTest") {
	group = "verification"
	description = "Run all functional tests across modules"
	dependsOn(
		subprojects.mapNotNull { it.tasks.findByName("functionalTest") }
	)
}


tasks.register<DefaultTask>("aggregateJavadoc") {
    group = "documentation"
    description = "Aggregates Javadoc from all subprojects."

    val outputDir = layout.buildDirectory.dir("docs/javadoc").get().asFile

    doLast {
        ant.withGroovyBuilder {
            "javadoc"(
                "destdir" to outputDir,
                "sourcepath" to subprojects.joinToString(separator = File.pathSeparator) {
                    it.the<SourceSetContainer>()["main"].allJava.srcDirs.joinToString(File.pathSeparator)
                },
                "classpath" to subprojects.joinToString(separator = File.pathSeparator) {
                    it.the<SourceSetContainer>()["main"].compileClasspath.asPath
                },
                "use" to true,
                "author" to true,
                "version" to true,
                "windowtitle" to "PickAndEat API Documentation"
            )
        }
    }
}


