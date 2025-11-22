# --- ETAPA 1: BUILD ---
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiamos solo pom.xml primero para cachear dependencias
COPY pom.xml ./
RUN mvn dependency:go-offline

# Ahora copiamos todo el proyecto
COPY . .

# Compilamos el paquete
RUN mvn clean package -DskipTests

# --- ETAPA 2: RUN ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/el_buen_corte-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
