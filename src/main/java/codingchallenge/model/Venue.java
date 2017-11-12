package codingchallenge.model;

import org.apache.log4j.Logger;

import java.util.*;

/**
 *  Venue holds the number of seats and seats per row
 */
public class Venue {

    private int totalAvailable;
    private HashMap<Row, List<Seat>> venueMap = new HashMap<>();
    private final static Logger logger = Logger.getLogger(Venue.class);


    public Venue(int rows, int seatsPerRow) {
        constructVenue(rows, seatsPerRow);
        this.totalAvailable = rows * seatsPerRow;
    }

    private void constructVenue(int rows, int seatsPerRow) {
        for(int i = 1; i <= rows; i++) {
            List<Seat> seats = new ArrayList<>();
            for(int j = 1; j <= seatsPerRow; j++) {
                seats.add(new Seat(j, Status.AVAILABLE));
            }
            Row r = new Row(i, seatsPerRow);
            venueMap.put(r,seats);
        }
    }

    public int getTotalAvailable() {
        return totalAvailable;
    }

    public HashMap<Row, List<Seat>> getVenueMap() {
        return venueMap;
    }

    public void incrementTotalAvailable(int numSeats) { this.totalAvailable = this.getTotalAvailable() + numSeats; }

    public void decrementTotalAvailable(int numSeats) { this.totalAvailable = this.getTotalAvailable() - numSeats; }

    public void print() {
        this.venueMap.entrySet().stream().forEach(row ->{
            logger.debug(row.getKey() + " " + row.getValue());
        });
    }
}
