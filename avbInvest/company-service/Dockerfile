FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/company-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

#COPY company-service.jar company-service.jar
#COPY wait-for-it.sh wait-for-it.sh
#RUN chmod +x wait-for-it.sh
#CMD ["sh", "-c", "./wait-for-it.sh config-server:8888 -- java -jar company-service.jar"]