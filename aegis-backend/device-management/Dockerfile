FROM java:8-jdk

EXPOSE 8003

RUN mkdir /app

WORKDIR /app

ARG JAR_FILE=target/*jar

COPY ${JAR_FILE} /app/devicemanagement-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/devicemanagement-0.0.1-SNAPSHOT.jar"]
