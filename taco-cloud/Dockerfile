FROM adoptopenjdk:11-jre-hotspot

EXPOSE 8080
COPY target/taco-cloud-0.0.1-SNAPSHOT.jar .
COPY src/main/resources/application-docker.properties .

CMD ["java", "-jar", "taco-cloud-0.0.1-SNAPSHOT.jar"]
