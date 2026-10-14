FROM maven:3.9.11-eclipse-temurin-25@sha256:407c4423cec0cf2981055bc2c6c0dc211d9605b6669279b95997f2d1c7e91e2c AS build
WORKDIR /work/apps/api
COPY apps/api/pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY apps/api/src ./src
RUN mvn -B -q package

FROM eclipse-temurin:25-jre-noble@sha256:d9a39a23634650173f1e2bbc176227af9728587ecf0f4b62d53e9355cd7a19ab AS runtime
WORKDIR /app
COPY --from=build /work/apps/api/target/capmoney-api-0.1.0-SNAPSHOT.jar /app/app.jar
RUN useradd --system --uid 10001 --home /app capmoney
USER capmoney
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM build AS probe-build
RUN mvn -B -q test-compile \
    && mkdir -p target/probe/BOOT-INF/classes/com/capmoney/api \
    && cp target/test-classes/com/capmoney/api/ProbeController.class target/probe/BOOT-INF/classes/com/capmoney/api/ \
    && jar -uf target/capmoney-api-0.1.0-SNAPSHOT.jar -C target/probe BOOT-INF/classes/com/capmoney/api/ProbeController.class

FROM runtime AS probe
USER root
COPY --from=probe-build /work/apps/api/target/capmoney-api-0.1.0-SNAPSHOT.jar /app/app.jar
USER capmoney
