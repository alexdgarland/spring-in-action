jenv exec mvn clean package -DskipTests
docker build -t taco-cloud .
