FROM sbtscala/scala-sbt:eclipse-temurin-focal-11.0.22_7_1.9.9_3.4.0
WORKDIR /home
COPY . /home

RUN sbt compile
RUN sbt assembly
RUN mv target/scala-3.4.0/manjuu-backend.jar manjuu-backend.jar
EXPOSE 8080
# CMD ["java", "-jar", "manjuu-backend.jar"]
