FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    maven \
    wget \
    git \
    curl

WORKDIR /app

COPY . .

RUN mvn clean install

FROM openjdk:21-jdk-slim AS runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Define o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
