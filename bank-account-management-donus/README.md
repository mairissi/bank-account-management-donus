Donus Banking Account Management
-

This is a Java REST API built using Maven, Docker, Springboot, SpringData, Lombok, OpenApi, Mockito and MySQL.

The application has some of most common functions related to managing a bank account, such as depoist, withdraw and transfer.\
For now it's only possible to make transfer for one Donus account to another.

About the project
-
To run the application you will need Docker installed in your computer.

1 - In the root project folder run maven to build and compile the application:
- mvn clean install

2 - Confirm that your docker is running. If you do not have docker installed, please see https://www.docker.com/get-started

3 - Start docker. Open a terminal, navegate to the root folder application and execute the command:
- docker-compose up --build --force-recreate

Swagger
 -
 Swagger can be access for the link http://localhost:8080/donus-api.html

Docker
-
I chose to create a volume, located in the project's docker folder, so that I could keep the data generated and saved in MySQL during the execution of the project.
So when running the project for the first time, it is already possible to test the services using both the swagger and the postman.

Postman
-
https://www.getpostman.com/collections/3ca30809bda2c3e6590e