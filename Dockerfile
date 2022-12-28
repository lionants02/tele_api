FROM openjdk:17-alpine
WORKDIR /nstda/tele_api
COPY build/libs/th.nstda.thongkum.tele_api-all.jar .
COPY src/main/resources/hikarisimple.properties .
EXPOSE 8080/tcp
CMD ["java", "-jar", "th.nstda.thongkum.tele_api-all.jar"]