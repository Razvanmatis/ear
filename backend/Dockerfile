FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /workspace

COPY backend ./backend
COPY openapi/openapi.yaml ./openapi/openapi.yaml

RUN mvn -B -f backend/pom.xml -DskipTests package

FROM eclipse-temurin:25-jre

WORKDIR /app

RUN useradd --system --uid 10001 --create-home app \
    && mkdir -p /app/data \
    && chown -R app:app /app

COPY --from=build --chown=app:app \
    /workspace/backend/target/mengenabgaben-1.0.0-SNAPSHOT.jar \
    /app/app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
