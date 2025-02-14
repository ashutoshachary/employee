# Use an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /tmp

# Copy the application JAR file to the container
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8083
