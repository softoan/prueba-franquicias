# Etapa 1: Compilación del proyecto
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos del proyecto
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# Compilar y empacar el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiar el jar generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
