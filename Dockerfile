FROM openjdk:11
EXPOSE 8090
ADD  /target/velorent-0.0.1-SNAPSHOT.jar velorent.jar
ENTRYPOINT ["java","-jar","velorent.jar"]