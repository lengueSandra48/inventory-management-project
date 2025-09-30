# Étape de build
FROM eclipse-temurin:22-jdk-jammy AS builder
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Étape de production
FROM eclipse-temurin:22-jre-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8881
ENTRYPOINT ["java", "-jar", "app.jar"]