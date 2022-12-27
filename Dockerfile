FROM openjdk:17-alpine
WORKDIR /nstda/tele_api
COPY build/libs/th.nstda.thongkum.tele_api-all.jar .
EXPOSE 8080/tcp
CMD ["java", "-jar", "th.nstda.thongkum.tele_api-all.jar"]