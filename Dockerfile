FROM maven:3.8.6-eclipse-temurin-17

WORKDIR /app
COPY . /app/

# Asegura que el script mvnw tenga permisos de ejecución
RUN chmod +x ./mvnw

# Compila la aplicación con un objetivo específico
RUN ./mvnw clean package -DskipTests

# Ejecuta la aplicación
EXPOSE 8080
CMD ["java", "-jar", "target/*.jar"]