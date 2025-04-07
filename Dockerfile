FROM maven:3.8.6-eclipse-temurin-17

WORKDIR /app
COPY . /app/

# Compila la aplicación con Maven directamente
RUN mvn clean package -DskipTests

# Ejecuta la aplicación
EXPOSE 8080
CMD ["sh", "-c", "java -jar target/*.jar"]