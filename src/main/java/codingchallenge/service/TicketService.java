package codingchallenge.service;

import codingchallenge.model.SeatHold;

public interface TicketService {

    /**
     * Check if the timer is expired or not.
     * @return a boolean flag to indicate if timer is expired or not
     */
    boolean isTimerExpired();
    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    int numSeatsAvailable();

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
     * information
     */
    SeatHold findAndHoldSeats(int numSeats, String customerEmail);

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHold   the seat hold object containing seats reserved
     * @return a reservation confirmation code
     */
    SeatHold reserveSeats(SeatHold seatHold);

    /**
     * Reset seats held for a specific customer after timer gets expired.
     * @param seatHold the seat hold object containing seats held
     */
    void resetHeldSeats(SeatHold seatHold);
}
