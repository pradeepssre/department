# Multi-stage build for Spring Boot application
# Stage 1: Build stage (if building from source)
FROM eclipse-temurin:21-jdk-alpine AS builder
# Install Maven
RUN apk add --no-cache maven

WORKDIR /app
COPY . .
# Build the application (uncomment if building from source)
# RUN ./gradlew clean build -x test
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Set JVM options for low memory environment
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=60s --timeout=5s --start-period=90s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application

ENTRYPOINT ["/bin/bash", "-c", "java $JAVA_OPTS -jar app.jar"]