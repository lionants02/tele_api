FROM openjdk:17-alpine
RUN apk add --no-cache curl
WORKDIR /nstda/tele_api
COPY build/libs/th.nstda.thongkum.tele_api-all.jar .
COPY src/main/resources/hikarisimple.properties .
COPY src/main/resources/hikarisimple.properties ./hikari_activity_log_db.properties
EXPOSE 8080/tcp
CMD ["java","-Xmx2000m","-Dfile.encoding=UTF-8", "-jar", "th.nstda.thongkum.tele_api-all.jar"]