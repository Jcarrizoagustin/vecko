FROM maven:3.8-openjdk-17 AS build

# Preinstalar extensiones de PostgreSQL si es necesario
RUN apt-get update && apt-get install -y postgresql-client libpq-dev

# Preparar la caché de dependencias de Maven para acelerar el proceso
WORKDIR /tmp/deps
COPY pom.xml .
RUN mvn dependency:go-offline

# Construir la aplicación
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

# Instalar dependencias de runtime para PostgreSQL
RUN apt-get update && apt-get install -y libpq5 && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]