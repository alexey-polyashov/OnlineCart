FROM java:8-jdk
VOLUME /tmp
ARG JAR_FILE=/target/*.jar
ARG IMG_FILES=src/main/resources/images/.
COPY ${JAR_FILE} /app/application.jar
ADD src/main/resources/images/500 /appdata/images/500
ADD src/main/resources/images/default /appdata/images/default
# ADD src/main/resources/images/ /appdata/images
WORKDIR /app
EXPOSE 8081
ENTRYPOINT ["java","-jar","application.jar","--spring.profiles.active=dev"]
