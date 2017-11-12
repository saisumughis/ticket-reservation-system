package codingchallenge.handlers;

import codingchallenge.ServerInit;
import codingchallenge.constants.Constants;
import codingchallenge.model.SeatHold;
import codingchallenge.model.Venue;
import codingchallenge.service.TicketService;
import codingchallenge.service.TicketServiceImpl;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Handles multiple incoming client connections and processes the inputs
 */
public class ServerHandler implements Runnable{

    private final static Logger logger = Logger.getLogger(ServerHandler.class);
    private Socket connection;
    private int id;
    private static Venue venue = ServerInit.venue;
    private static HashMap<String, LinkedHashSet<SeatHold>> customerReservationInfoMap = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();
    private TicketService ticketService;
    private String emailId;
    private Timer timer;


    public ServerHandler(Socket connection, int id) {
        this.connection = connection;
        this.id = id;
        this.ticketService = new TicketServiceImpl(venue);
    }

    @Override
    public synchronized void run() {
        DataInputStream inputStream;
        DataOutputStream outputStream;
        logger.info("Received a connection from "
                + connection.getInetAddress().getHostName() + ":"
                + connection.getPort());
        while(true) {
            try {
                inputStream = new DataInputStream(connection.getInputStream());
                outputStream = new DataOutputStream(connection.getOutputStream());
                String input = inputStream.readUTF();
                logger.info("Input from client:" + id + " " + input);
                String message = processClientInput(input);
                outputStream.writeUTF(message);
            }catch (EOFException e) {
                logger.info("Client closed its session!");
                break;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Process the user requests
     * @param input the input received from the user
     * @return the message after processing user input
     */
    private synchronized String processClientInput(String input) {

        if(input.contains("REGISTER")) {
            String message;
            String inputEmailID = input.split(Constants.delimiter)[1];
            if(isUserExists(inputEmailID)) {
                emailId = inputEmailID;
                message = "EmailID is already registered.\n";
                return message + getReservationDetails();
            }else {
                emailId = inputEmailID;
                updateUserInfo(emailId, null);
                message = "Registered Successfully.";
            }
            return message;
        }else if(input.contains("BOOK")){
            int numSeats = Integer.parseInt(input.split(Constants.delimiter)[1]);
            if(venue.getTotalAvailable() == 0) {
                return "Sorry, All tickets Sold! Please try again later!";
            }
            if (numSeats > venue.getTotalAvailable()) {
                return "Seats available:" + venue.getTotalAvailable();
            }
            SeatHold seatHold = this.ticketService.findAndHoldSeats(numSeats, emailId);
            updateUserInfo(emailId,seatHold);
            startTimer(seatHold);
            return seatHold.responseToUser();
        }else if(input.equals("RESERVE")) {
            if(ticketService.isTimerExpired())
                return "Session Expired. Please book again.";
            timer.cancel();
            SeatHold lastHeldSeat = (SeatHold) getUserInfo(emailId).toArray()[getUserInfo(emailId).size() - 1];
            SeatHold seatHold = this.ticketService.reserveSeats(lastHeldSeat);
            updateUserInfo(emailId,seatHold);
            return seatHold.responseToUser();
        }else if(input.contains("VIEW")) {
                return getReservationDetails();
        } else if(input.contains("SHOW TOTAL")) {
            return "Seats available:" + venue.getTotalAvailable();
        }else if(input.contains("CANCEL")) {
            if(!ticketService.isTimerExpired()) {
                SeatHold lastHeldSeat = (SeatHold) getUserInfo(emailId).toArray()[getUserInfo(emailId).size() - 1];
                timer.cancel();
                ticketService.resetHeldSeats(lastHeldSeat);
                removeSeatHeld(emailId, lastHeldSeat);
            }
            return getReservationDetails();
        }
        return "";
    }

    /**
     *  Gets the list of reservations by a user
     * @return Reservation details
     */

    private String getReservationDetails() {
        LinkedHashSet<SeatHold> seatHolds = getUserInfo(emailId);
        String res;
        if(seatHolds.size() > 0) {
            res = "\n ****** Reservation Details ****** \n";
            for (SeatHold seatHold : seatHolds) {
                res += seatHold.responseToUser() + "\n";
            }
            res += "*******************";
        }else {
            return "You currently have no reservation(s).";
        }
        return res;
    }

    /**
     * Checks if the user is already registered.
     * @param emailId Email id of the user
     * @return flag that indicates if the user is registered or not
     */
    private Boolean isUserExists(String emailId) {
        lock.lock();
        try {
            if (customerReservationInfoMap.containsKey(emailId)) {
                return true;
            }
        }finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * Updates the seat hold information of the user
     * @param emailId The email id of the user
     * @param seatHold The seathold object to be updated
     * @return seatHold The updated seathold object
     */
    private SeatHold updateUserInfo(String emailId, SeatHold seatHold) {
        lock.lock();
        try {
            if (seatHold == null) {
                customerReservationInfoMap.put(emailId, new LinkedHashSet<>());
            } else {
                LinkedHashSet<SeatHold> seatHolds = customerReservationInfoMap.get(emailId);
                seatHolds.add(seatHold);
            }
        }finally {
            lock.unlock();
        }
        return seatHold;
    }

    /**
     * Removes the seat hold information
     * @param emailId The email id of the user
     * @param seatHold The seathold object to be updated
     */
    private void removeSeatHeld(String emailId, SeatHold seatHold) {
        lock.lock();
        try {
            LinkedHashSet<SeatHold> seatHolds = customerReservationInfoMap.get(emailId);
            seatHolds.remove(seatHold);
        }finally {
            lock.unlock();
        }
    }

    /**
     * Gets the information of the user.
     * @param emailId The email id of the user
     * @return The list of reservations by the user
     */
    private LinkedHashSet<SeatHold> getUserInfo(String emailId) {
        lock.lock();
        try {
            if (customerReservationInfoMap.containsKey(emailId)) {
                return customerReservationInfoMap.get(emailId);
            }
        }finally {
            lock.unlock();
        }
        return null;
    }

    /**
     * Holds the seats for a specified timeout
     * @param seatHold seathold object
     */
    private void startTimer(SeatHold seatHold) {
        timer = new Timer();

        timer.schedule( new TimerTask(){
            public void run() {
                // reset the held seats to available and remove the seats held
                logger.info("Releasing hold on seats...");
                ticketService.resetHeldSeats(seatHold);
                removeSeatHeld(emailId, seatHold);
                logger.info("Session Expired!");
                logger.info("total available:" + ticketService.numSeatsAvailable());
            }

        }, 1000 * Constants.TIMEOUT);

    }

}
