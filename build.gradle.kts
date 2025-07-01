plugins {
	java
	id("io.spring.dependency-management") version "1.1.7"
	id("co.uzzu.dotenv.gradle") version "4.0.0"
	id("com.diffplug.spotless") version "7.0.4"
	id("pl.allegro.tech.build.axion-release") version "1.18.18"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

version = scmVersion.version

scmVersion {
	tag {
		prefix = "v"
	}

	versionCreator("simple")
	useHighestVersion = true

}



allprojects {
	group = "com.pickandeat"
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin= "jacoco")
	apply(plugin = "com.diffplug.spotless")

	dependencyManagement {
		imports {
			mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.5")
		}
	}

	spotless {
		java {
			googleJavaFormat("1.17.0")
			target("src/**/*.java")
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
	}


	plugins.withId("java") {
		val coverageLimit: BigDecimal =
			(project.findProperty("limit") as String?)?.toBigDecimalOrNull() ?: BigDecimal("0.8")

		val unitTest: TaskProvider<Test> = tasks.register("unitTest", Test::class) {
			useJUnitPlatform {
				includeTags("unit")
			}
		}

		val jacocoUnitTestReport = tasks.register<JacocoReport>("jacocoUnitTestReport") {
			dependsOn(unitTest)
			reports {
				xml.required.set(true)
				html.required.set(true)
			}
			classDirectories.setFrom(
				layout.files(layout.buildDirectory.dir("classes/java/main")).asFileTree.matching {
					exclude("**/generated/**")
				}
			)
			sourceDirectories.setFrom(files("src/main/java"))
			executionData.setFrom(fileTree(layout.buildDirectory).include("jacoco/unitTest.exec"))
		}

		val jacocoUnitTestCoverageVerification =
			tasks.register<JacocoCoverageVerification>("jacocoUnitTestCoverageVerification") {
				dependsOn(unitTest)
				violationRules {
					rule {
						limit {
							minimum = coverageLimit
						}
					}
				}
				classDirectories.setFrom(
					layout.files(layout.buildDirectory.dir("classes/java/main")).asFileTree.matching {
						exclude("**/generated/**")
					}
				)
				sourceDirectories.setFrom(files("src/main/java"))
				executionData.setFrom(fileTree(layout.buildDirectory).include("jacoco/unitTest.exec"))
			}

		unitTest.configure {
			finalizedBy(jacocoUnitTestReport, jacocoUnitTestCoverageVerification)
		}
	}

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


