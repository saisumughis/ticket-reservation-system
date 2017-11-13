Ticket Reservation System

Server Start Up:

cd ticket-reservation-system
mvn package
java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ServerInit serverPort numOfRows seatsPerRow


Multiple Clients can be started simultaneously in different terminals.

Client Start Up:

cd ticket-reservation-system
java -cp target/codingchallenge-1.0-SNAPSHOT-jar-with-dependencies.jar codingchallenge.ClientInit serverHost serverPort

Running unit tests
mvn test
