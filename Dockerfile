FROM alpine/git as clone
WORKDIR /app
RUN git clone https://github.com/jonathanackerson/nasa-marsrover-photos.git

FROM maven:3.5-jdk-11 as build
WORKDIR /app
COPY --from=clone /app/nasa-marsrover-photos /app
RUN mkdir -p /app/photos/rover
RUN mvn install

FROM openjdk:11-jre
WORKDIR /app
COPY --from=build /app/target/marsroverphotos-0.0.1-SNAPSHOT.jar /app
RUN mkdir -p /app/photos/rover
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "marsroverphotos-0.0.1-SNAPSHOT.jar"]