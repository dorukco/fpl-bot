FROM eclipse-temurin:21-jre
COPY build/libs/fplbot-*.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xmx256m", "-jar", "app.jar"]