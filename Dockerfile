


FROM openjdk:21

WORKDIR /app

COPY target/*.jar .

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "scholarship-management-1.0.jar" ,"--spring.profiles.active=prod"]
