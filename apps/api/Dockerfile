FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /work/apps/api
COPY apps/api/pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY apps/api/src ./src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:25-jre-noble AS runtime
WORKDIR /app
COPY --from=build /work/apps/api/target/capmoney-api-0.1.0-SNAPSHOT.jar /app/app.jar
RUN useradd --system --uid 10001 --home /app capmoney
USER capmoney
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM build AS probe-build
RUN mvn -B -q test-compile && jar -uf target/capmoney-api-0.1.0-SNAPSHOT.jar -C target/test-classes com/capmoney/api/ProbeController.class

FROM runtime AS probe
USER root
COPY --from=probe-build /work/apps/api/target/capmoney-api-0.1.0-SNAPSHOT.jar /app/app.jar
USER capmoney
