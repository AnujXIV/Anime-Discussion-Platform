# Use official JDK image
FROM openjdk:21

# Set working directory
WORKDIR /app

# Copy JAR file built by Maven
COPY target/anime-fan-0.0.1-SNAPSHOT.jar app.jar

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
