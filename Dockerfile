# Etapa 1: Build
FROM maven:3.9.10-eclipse-temurin-21 AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de Maven primero para aprovechar la cache
COPY pom.xml .
COPY mvnw .
COPY .mvn/ .mvn/

# Instalamos dependencias (sin compilar aún)
RUN mvn dependency:go-offline

# Copiamos todo el proyecto
COPY src/ src/

# Compilamos y generamos el jar
RUN mvn clean package -DskipTests

# Etapa 2: Run
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiamos el jar compilado desde la etapa build
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que usa tu app (cámbialo si tu app usa otro)
EXPOSE 8082

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
