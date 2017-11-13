# Ticket Reservation System
A simple ticket reservation system that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venue. The project is implemented using socket programming in Java 8. The build tool used is Maven.

## Prerequisite
Java 8
Maven

## Instructions
Server Start Up Command
```
cd ticket-reservation-system 
mvn package
java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ServerInit port rows seats

Example: 
java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ServerInit 9999 10 20
``` 

Multiple Clients can be started simultaneously in different terminals.

Client Start Up Command

```
cd ticket-reservation-system
java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ClientInit serverHost serverPort

Example:
java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ClientInit localhost 9999
```

Running Unit Tests

```
mvn test
```
