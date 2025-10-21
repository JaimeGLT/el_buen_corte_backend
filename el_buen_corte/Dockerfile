FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/el_buen_corte-0.0.1-SNAPSHOT.jar el_buen_corte.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","el_buen_corte.jar"]