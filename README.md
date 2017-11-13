# Ticket Reservation System

## Server Start Up Command

cd ticket-reservation-system 

mvn package

java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ServerInit port rows seats

port - server port

rows - number of rows

seats - number of seats per row

Example Usage: 

java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ServerInit 9999 10 20
 

Multiple Clients can be started simultaneously in different terminals.

## Client Start Up Command

cd ticket-reservation-system

java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ClientInit serverHost serverPort

serverHost: localhost

serverPort: 9999

Example Usage:
java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ClientInit localhost 9999


## Running Unit Tests

mvn test
