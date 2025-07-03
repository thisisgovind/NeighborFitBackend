FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build the app
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["java", "-jar", "target/neighborfit-0.0.1-SNAPSHOT.jar"]
