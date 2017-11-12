package codingchallenge;

import codingchallenge.constants.Constants;
import codingchallenge.handlers.ServerHandler;
import codingchallenge.model.Venue;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  Initializes server and creates a socket connection to handle clients
 */
public class ServerInit {

    private static int port = Constants.SERVERPORT;
    private static int rows = Constants.ROWS;
    private static int seatsPerRow = Constants.SEATSPERROW;
    public static Venue venue = new Venue(rows,seatsPerRow);
    private final static Logger logger = Logger.getLogger(ServerInit.class);

    public static void main(String[] args) {
        int count  = 0;
        try {
            if(args.length < 3) {
                logger.info("Using default values");
                logger.info("No of rows: " +  rows);
                logger.info("No of seats per row: " +  seatsPerRow);
            }
            else if(args.length == 3) {
                port = Integer.parseInt(args[0]);
                rows = Integer.parseInt(args[1]);
                seatsPerRow = Integer.parseInt(args[2]);
                venue = new Venue(rows,seatsPerRow);
                logger.info("No of rows: " +  rows);
                logger.info("No of seats per row: " +  seatsPerRow);
            } else {
                logger.error("Usage: java ServerInit port rows seatsperrow");
                return;
            }

            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("Ticketing Server is listening on port " + port);
            logger.info("Waiting for incoming client connections..");

            while (true) {
                Socket connection = serverSocket.accept();
                ServerHandler serverHandler = new ServerHandler(connection, ++count);
                Thread thread = new Thread(serverHandler);
                thread.start();
            }
        } catch (IOException e) {
            logger.error("Unable to create socket");
            e.printStackTrace();
            System.exit(0);
        }
    }

}
