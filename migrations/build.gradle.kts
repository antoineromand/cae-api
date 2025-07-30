plugins {
    `java-library`
}

dependencies {
}

java {
    sourceSets["main"].resources.srcDirs("src/main/resources")
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Copy>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}