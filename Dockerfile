# BUILD
FROM eclipse-temurin:24-jdk-alpine AS build

RUN apk add --no-cache bash unzip wget


WORKDIR /app
COPY gradlew gradle* /app/
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle /app/gradle

COPY shared /app/shared
COPY authentication /app/authentication
COPY api /app/api

RUN chmod +x ./gradlew

RUN ./gradlew clean :api:bootJar -x test --no-daemon --no-build-cache

#FINAL
FROM eclipse-temurin:24-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/api/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]


