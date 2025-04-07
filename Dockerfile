ROM maven:3.8.6-eclipse-temurin-17

WORKDIR /app
COPY . /app/

# Asegura que el script mvnw tenga permisos de ejecución
RUN chmod +x ./mvnw