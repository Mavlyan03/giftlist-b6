FROM maven:3.8.6-jdk-11-slim AS build

WORKDIR /b6/build

COPY . /b6/build

RUN mvn clean install -DskiptTests=true

FROM openjdk:11.0.11-jre-slim
WORKDIR /b6/app

COPY --from=build /b6/build/target/taigan.jar /onroad/app/

EXPOSE 8080

ENTRYPOINT ["java","-jar","/b6/app/taigan.jar"]