package codingchallenge.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import codingchallenge.constants.Constants;
import org.apache.log4j.Logger;

/**
 *  Holds user reservation details. A user can have multiple reservations.
 */
public class SeatHold {

    private Integer seatHoldId;
    private String customerEmail;
    private int numSeats;
    private HashMap<Integer, String> seatsHeldOrReserved;
    private static final AtomicInteger count = new AtomicInteger(100);
    private static final Logger logger =  Logger.getLogger(SeatHold.class);

    public SeatHold(String customerEmail, int numSeats) {
        this.seatHoldId = count.getAndIncrement();
        this.customerEmail = customerEmail;
        this.numSeats = numSeats;
        this.seatsHeldOrReserved = new HashMap<>();
    }

    public Integer getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(Integer seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public HashMap<Integer, String> getSeatsHeldOrReserved() {
        return seatsHeldOrReserved;
    }

    public void setSeatsHeldOrReserved(HashMap<Integer, String> seatsHeldOrReserved) {
        this.seatsHeldOrReserved = seatsHeldOrReserved;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    @Override
    public String toString() {
        return "SeatHold{" +
                "seatHoldId=" + seatHoldId +
                ", customerEmail='" + customerEmail + '\'' +
                ", seatsHeldOrReserved=" + seatsHeldOrReserved +
                '}';
    }

    public void print(){
        this.seatsHeldOrReserved.forEach((key,value) -> {
            logger.info("Row number:" +  key);
            logger.info(" Seat number(s): " + value);
        });
    }

    public String responseToUser() {
        String res = "Reservation Id:" +  this.seatHoldId + " ";
        Set<Map.Entry<Integer,String>> set  = this.seatsHeldOrReserved.entrySet();
        for(Map.Entry entry: set) {
            res += "Row Number:" + entry.getKey() + " ";
            String[] seatsRange = entry.getValue().toString().split(Constants.delimiter);
            if(!seatsRange[0].equals(seatsRange[1]))
                res += "Seat Number(s):" + entry.getValue() + " ";
            else
                res += "Seat Number:" + seatsRange[0] + " ";
        }
        return res;
    }

}
