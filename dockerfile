FROM eclipse-temurin:17-jdk as build

COPY metamesh /MetaMesh

WORKDIR /MetaMesh

RUN ./mvnw package -DskipTests

RUN mv -f target/*.jar /MetaMesh.jar

FROM eclipse-temurin:21-jre

ARG PORT
ENV PORT=${PORT}

COPY --from=build /MetaMesh.jar .

RUN useradd runtime
USER runtime

ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "MetaMesh.jar" ]
