FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /work
COPY --from=build /build/target/quarkus-app/lib/ /work/lib/
COPY --from=build /build/target/quarkus-app/*.jar /work/
COPY --from=build /build/target/quarkus-app/app/ /work/app/
COPY --from=build /build/target/quarkus-app/quarkus/ /work/quarkus/
EXPOSE 8080
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["java","-jar","/work/quarkus-run.jar"]
