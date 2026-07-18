# --- Build Stage ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the JAR file without running tests to speed up deployment
RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:17-jdk-jammy

# Hugging Face security requirement: run as a non-root user with ID 1000
RUN useradd -m -u 1000 user
USER user
ENV HOME=/home/user
WORKDIR $HOME/app

# Copy the built JAR from the build stage and give ownership to 'user'
COPY --from=build --chown=user /app/target/*.jar app.jar

# Expose the mandatory Hugging Face port
EXPOSE 7860

# Launch Spring Boot and force it to listen on port 7860
ENTRYPOINT ["java", "-Dserver.port=7860", "-jar", "app.jar"]
