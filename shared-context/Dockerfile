FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml

RUN ./mvnw dependency:go-offline

COPY . .

RUN ./mvnw clean deploy -DskipTests