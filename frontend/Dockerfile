# Build stage
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies (optional but good)
COPY pom.xml .
# Let mvn resolve dependencies during package (no separate go-offline)
COPY src ./src

# Build the application (skip tests to speed up)
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17-alpine
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]