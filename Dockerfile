FROM maven:3.9.9-amazoncorretto-21-alpine AS build
#FROM maven:3.9.9-amazoncorretto-21-alpine

COPY pom.xml /opt/gogobot/

WORKDIR /opt/gogobot/

RUN mvn dependency:resolve

COPY src /opt/gogobot/src/

RUN mvn clean package

RUN echo "Maven package completed!"

FROM eclipse-temurin:21-jre

COPY --from=build /opt/gogobot/target/gogobot-*.jar /opt/app.jar

COPY gogobot.sh /usr/bin/gogobot

RUN chmod +x /usr/bin/gogobot
