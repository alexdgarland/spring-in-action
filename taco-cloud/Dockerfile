FROM adoptopenjdk:11-jre-hotspot

EXPOSE 8080
COPY target/taco-cloud-0.0.1-SNAPSHOT.jar .
COPY container_scripts/run_app.sh .

CMD ["./run_app.sh"]
