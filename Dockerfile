# Use an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/employeetesting-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables from a .env file
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
