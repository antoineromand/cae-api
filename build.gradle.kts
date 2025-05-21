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

allprojects {
	group = "com.pickandeat"
	version = "0.0.1-SNAPSHOT"
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")
	dependencyManagement {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.5")
			mavenBom("org.springframework.modulith:spring-modulith-bom:1.3.5")
		}
	}
	dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
	tasks.withType<Test> {
	useJUnitPlatform()
	}
}


