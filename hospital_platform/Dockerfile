#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src /home/app/src
RUN mvn -Dmaven.test.skip package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/hospital_platform-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]