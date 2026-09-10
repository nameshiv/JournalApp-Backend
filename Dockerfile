FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-Xms128m", "-Xmx300m", "-jar", "target/journalApp-0.0.1-SNAPSHOT.jar"]