FROM eclipse-temurin:21-jdk as builder

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml

RUN ./mvnw dependency:go-offline

COPY . .
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]