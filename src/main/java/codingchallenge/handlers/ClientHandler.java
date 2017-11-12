package codingchallenge.handlers;


import codingchallenge.constants.Constants;
import codingchallenge.constants.EmailValidator;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *  Handles client inputs
 */
public class ClientHandler implements Runnable {

    private final static Logger logger = Logger.getLogger(ClientHandler.class);
    private String hostname;
    private int port;
    private Socket connection;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private EmailValidator emailValidator = new EmailValidator();

    public ClientHandler(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run() {
        logger.info("Establishing connection. Please wait ...");
        try
        {
            connection = new Socket(this.hostname, this.port);
            logger.info("Connected: " + connection);
            inputStream = new DataInputStream(connection.getInputStream());
            outputStream = new DataOutputStream(connection.getOutputStream());
            String input;
            String sendToServer;
            Scanner scanner = new Scanner(System.in);
            logger.info("Welcome to ticketing system!");
            while(true) {
                logger.info("Please enter your email id to register");
                input = scanner.nextLine();
                if(!emailValidator.validateEmail(input)) {
                    logger.error("Invalid Email Id");
                    continue;
                }else {
                    break;
                }
            }
            sendToServer = "REGISTER" + Constants.delimiter + input;
            outputStream.writeUTF(sendToServer);
            logger.info(inputStream.readUTF());
            while (true) {
                String userDisplay = "\n---------- Enter your choice: ---------\n";
                userDisplay += "1. Book Seats\n";
                userDisplay += "2. Show total Available Seats\n";
                userDisplay += "3. View Reservation\n";
                userDisplay += "4. Exit\n";
                userDisplay += "--------------------------------------";
                logger.info(userDisplay);
                int choice;
                try {
                     choice = Integer.parseInt(scanner.nextLine());
                }catch (NumberFormatException e) {
                    logger.info("Invalid choice.");
                    continue;
                }
                switch (choice) {
                    case 1:
                        logger.info("Enter the number of seats to book");
                        int numSeats;
                        try {
                             numSeats = Integer.parseInt(scanner.nextLine());
                        }catch (NumberFormatException e) {
                            logger.error("Invalid choice.");
                            break;
                        }
                        if (numSeats <= 0) {
                            logger.error("Enter a valid choice");
                            continue;
                        }
                        sendToServer = "BOOK" + Constants.delimiter + String.valueOf(numSeats);
                        outputStream.writeUTF(sendToServer);
                        String serverMsg = inputStream.readUTF();
                        logger.info(serverMsg);
                        if(serverMsg.startsWith("Reservation")) {
                            while(true) {
                                logger.info("You have upto " + Constants.TIMEOUT + " seconds to confirm your seats. Please type yes or no");
                                String userConfirmation = scanner.nextLine();
                                if (userConfirmation.equalsIgnoreCase("yes")) {
                                    sendToServer = "RESERVE";
                                    outputStream.writeUTF(sendToServer);
                                    logger.info(inputStream.readUTF());
                                    break;
                                } else if (userConfirmation.equalsIgnoreCase("no")) {
                                    sendToServer = "CANCEL";
                                    logger.info("Cancelling held seats..");
                                    outputStream.writeUTF(sendToServer);
                                    logger.info(inputStream.readUTF());
                                    break;
                                }else {
                                    logger.info("Invalid choice");
                                    continue;
                                }
                            }
                        }
                        break;
                    case 2:
                        sendToServer = "SHOW TOTAL";
                        outputStream.writeUTF(sendToServer);
                        logger.info(inputStream.readUTF());
                        break;
                    case 3:
                        sendToServer = "VIEW";
                        outputStream.writeUTF(sendToServer);
                        logger.info(inputStream.readUTF());
                        break;
                    case 4:
                        logger.info("Thanks for using our application!");
                        System.exit(0);
                    default:
                        logger.error("Invalid Choice");
                        break;
                }
            }
        }
        catch(UnknownHostException e) {
            logger.error("Host unknown: " + e.getMessage());
        }
        catch(IOException e) {
            logger.error("Unexpected exception: " + e.getMessage());
        }
        finally {
            logger.info("Thanks for using our system!!");
        }
    }

}
