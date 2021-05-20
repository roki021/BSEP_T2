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
COPY pki /usr/local/lib/pki
COPY --from=build /home/app/target/platform-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/usr/local/lib/demo.jar"]
# "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"