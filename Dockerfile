FROM eclipse-temurin:21.0.9_10-jre-alpine-3.23
WORKDIR /app
RUN mkdir -p /app/certificates
COPY src/main/resources/supabase-crt/prod-ca-2021.crt /app/certificates/prod-ca-2021.crt
COPY target/*.jar /app/prediction.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/prediction.jar"]
# docker build --platform=linux/amd64 -t ddnnpp/prediction:latest .
