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
            while (true) {
                logger.info("Enter your choice:\n");
                logger.info("1. Register");
                logger.info("2. Book");
                logger.info("3. Reserve");
                logger.info("4. View Reservation");
                logger.info("5. Exit");
                Scanner scanner = new Scanner(System.in);
                int choice;
                try {
                     choice = Integer.parseInt(scanner.nextLine());
                }catch (NumberFormatException e) {
                    logger.info("Invalid choice.");
                    continue;
                }

                String sendToServer;

                switch (choice) {
                    case 1:
                        String input;
                        while(true) {
                            logger.info("Enter your email id");
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
                        break;
                    case 2:
                        logger.info("Enter the number of seats to book");
                        int numSeats = 0;
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
                        logger.info(inputStream.readUTF());
                        break;
                    case 3:
                        sendToServer = "RESERVE";
                        outputStream.writeUTF(sendToServer);
                        logger.info(inputStream.readUTF());
                        break;
                    case 4:
                        sendToServer = "VIEW";
                        outputStream.writeUTF(sendToServer);
                        logger.info(inputStream.readUTF());
                        break;
                    case 5:
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
