FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:11.0-jdk21-temurin
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/EmployeeService.war
EXPOSE 8086