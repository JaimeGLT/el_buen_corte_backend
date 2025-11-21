# --- ETAPA 1: BUILD (Construcción) ---
# Usamos una imagen oficial de Maven con Eclipse Temurin (sólida como una roca)
# Nombramos esta etapa como 'build' para referenciarla luego
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Primero copiamos solo el pom.xml para aprovechar el caché de capas de Docker
COPY pom.xml .
# Descargamos dependencias (esto hace que los builds futuros sean rapidísimos si no cambias el pom)
RUN mvn dependency:go-offline

# Ahora copiamos el código fuente
COPY src ./src

# Compilamos el paquete ignorando los tests (asumo que ya corriste los tests en tu CI/CD, ¿verdad?)
RUN mvn clean package -DskipTests

# --- ETAPA 2: RUN (Ejecución) ---
# Aquí usamos tu imagen corregida, solo el JRE para que sea ligero
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiamos el JAR generado en la etapa 'build'.
# OJO: Asegúrate que el nombre del jar en target coincida. 
# Si usas <finalName> en tu pom.xml, ajusta esta línea.
# El asterisco *.jar ayuda si cambia la versión, pero es mejor ser explícito.
COPY --from=build /app/target/el_buen_corte-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]