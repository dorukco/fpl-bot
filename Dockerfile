FROM --platform=linux/amd64 adoptopenjdk/openjdk11:jdk-11.0.4_11-alpine-slim
COPY build/libs/fplbot-*.jar app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Xmx256m -jar app.jar