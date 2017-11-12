package codingchallenge.service;

import codingchallenge.constants.Constants;
import codingchallenge.model.*;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class TicketServiceImpl implements TicketService {

    private Venue venue;
    private boolean isTimerExpired = false;
    private final static Logger logger = Logger.getLogger(TicketServiceImpl.class);

    public TicketServiceImpl(Venue venue) {
        this.venue = venue;
    }

    public int numSeatsAvailable() {
        return venue.getTotalAvailable();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        SeatHold seatHold = new SeatHold(customerEmail, numSeats);
        while(numSeats != 0) {

            // perform a binary search to get the best row
            Row selectedRow = performBinarySearch(numSeats);
            List<Seat> seats = venue.getVenueMap().get(selectedRow);

            // find the begin and end seat id based on available seats.
            int beginSeatId = seats.size() - selectedRow.getAvailableCount() + 1;
            int allocatedSeats = Math.min(numSeats, selectedRow.getAvailableCount());
            int endSeatId = beginSeatId +  allocatedSeats - 1;

            // hold the seats
            seatHold.getSeatsHeldOrReserved().put(selectedRow.getId(), String.valueOf(beginSeatId + Constants.delimiter + endSeatId));

            // change status of seats to HELD
            seats.subList(beginSeatId - 1, endSeatId).stream().forEach((seat) -> seat.setStatus(Status.HELD));

            // decrement available counts
            selectedRow.decrementAvailableCount(allocatedSeats);
            venue.decrementTotalAvailable(allocatedSeats);

            numSeats -= allocatedSeats;
        }
        logger.debug("************** Venue state after booking**********");
        venue.print();
        logger.debug("*************************");
        isTimerExpired = false;
        return seatHold;
    }

    private Row performBinarySearch(int numSeats) {

        List<Row> rows = venue.getVenueMap().keySet().stream().filter(row -> row.getAvailableCount() > 0)
                .collect(Collectors.toList());
        if(numSeats < rows.get(0).getAvailableCount()) {
            return rows.get(0);
        }
        if(numSeats > rows.get(rows.size()-1).getAvailableCount()) {
            return rows.get(rows.size()-1);
        }
        int low = 0;
        int high = rows.size()-1;
        while (high >= low)
        {
            int middle = (low + high) / 2;
            if (rows.get(middle).getAvailableCount() == numSeats)
            {
                return rows.get(middle);

            } else if (rows.get(middle).getAvailableCount() < numSeats)
            {
                low = middle + 1;
            }
            else if (rows.get(middle).getAvailableCount() > numSeats)
            {
                high = middle - 1;
            }
        }
        return (rows.get(low).getAvailableCount() - numSeats) < (numSeats - rows.get(high).getAvailableCount()) ? rows.get(high) : rows.get(low);
    }

    public void resetHeldSeats(SeatHold seatHold) {

        seatHold.getSeatsHeldOrReserved().entrySet().forEach(selectedRow -> {

            updateHeldSeatStatus(selectedRow, Status.HELD, Status.AVAILABLE);

            venue.getVenueMap().entrySet().forEach(row -> {
                if(row.getKey().getId() == selectedRow.getKey()) {
                    row.getKey().incrementAvailableCount(Constants.getNumSeatsHeldOrReserved(selectedRow.getValue()));
                }
            });
        });
        isTimerExpired = true;
        venue.incrementTotalAvailable(seatHold.getNumSeats());

        logger.debug("************** Venue state after resetting held seats**********");
        venue.print();
        logger.debug("*************************");
    }

    public SeatHold reserveSeats(SeatHold seatHold) {
        seatHold.getSeatsHeldOrReserved().entrySet().forEach((entry) -> {
            updateHeldSeatStatus(entry, Status.HELD, Status.RESERVED);
        });
        seatHold.print();
        logger.debug("************** Venue state after reserving**********");
        venue.print();
        logger.debug("*************************");
        return seatHold;
    }

    public boolean isTimerExpired() {
        return isTimerExpired;
    }

    private void updateHeldSeatStatus(Map.Entry<Integer, String> entry, Status fromStatus, Status toStatus) {
        venue.getVenueMap().get(new Row(entry.getKey()))
                .subList(Integer.parseInt(entry.getValue().split(Constants.delimiter)[0]) - 1,
                        Integer.parseInt(entry.getValue().split(Constants.delimiter)[1]))
                .stream()
                .forEach((seat) -> {
                    if(seat.getStatus() == fromStatus) {
                        seat.setStatus(toStatus);
                    }
                });
    }
}
