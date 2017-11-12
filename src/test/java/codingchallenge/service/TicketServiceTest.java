package codingchallenge.service;

import codingchallenge.constants.Constants;
import codingchallenge.model.Row;
import codingchallenge.model.SeatHold;
import codingchallenge.model.Status;
import codingchallenge.model.Venue;
import org.junit.Assert;
import org.junit.Test;


public class TicketServiceTest {

    @Test
    public void testNumSeatsAvailable() {
        int rows = 10;
        int seatsPerRow = 10;
        Venue venue = new Venue(rows, seatsPerRow);
        venue.print();
        TicketService ticketService = new TicketServiceImpl(venue);
        int total = ticketService.numSeatsAvailable();
        Assert.assertTrue("Total available count does not match", total == rows * seatsPerRow);
    }

    @Test
    public void testSingleRowReservation() {
        int rows = 1;
        int seatsPerRow = 5;
        Venue venue = new Venue(rows, seatsPerRow);
        venue.print();
        TicketService ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "sai");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 0);
        validateSeats(seatHold, venue, Status.HELD);

        seatHold = ticketService.reserveSeats(seatHold);
        validateSeats(seatHold, venue, Status.RESERVED);
    }

    @Test
    public void testMultiRowReservation() {
        int rows = 2;
        int seatsPerRow = 5;
        Venue venue = new Venue(rows, seatsPerRow);
        venue.print();
        TicketService ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(10, "sai");
        

        validateSeats(seatHold, venue, Status.HELD);

        seatHold = ticketService.reserveSeats(seatHold);
        validateSeats(seatHold, venue, Status.RESERVED);
    }

    @Test
    public void testResetHeldReservation() {
        int rows = 1;
        int seatsPerRow = 5;
        Venue venue = new Venue(rows, seatsPerRow);
        venue.print();
        TicketService ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "sai");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 0);
        validateSeats(seatHold, venue, Status.HELD);

        ticketService.resetHeldSeats(seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 5);
        validateSeats(seatHold, venue, Status.AVAILABLE);
    }

    @Test
    public void testMulipleUserReservation() {
        int rows = 5;
        int seatsPerRow = 10;
        Venue venue = new Venue(rows, seatsPerRow);
        venue.print();
        TicketService ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(10, "user1");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 40);

        seatHold = ticketService.findAndHoldSeats(5, "user2");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 35);


        seatHold = ticketService.findAndHoldSeats(4, "user3");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 31);

        seatHold = ticketService.findAndHoldSeats(10, "user4");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 21);

        seatHold = ticketService.findAndHoldSeats(2, "user5");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 19);
    }

    @Test
    public void testMiniumNoOfRows() {
        int rows = 1;
        int seatsPerRow = 10;
        Venue venue = new Venue(rows, seatsPerRow);
        venue.print();
        TicketService ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "sai");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 5);
        validateSeats(seatHold, venue, Status.HELD);

        seatHold = ticketService.findAndHoldSeats(5, "user2");
        
        Assert.assertNotNull("Seats held should not be null", seatHold);
        Assert.assertTrue("Available count is incorrect", ticketService.numSeatsAvailable() == 0);
        validateSeats(seatHold, venue, Status.HELD);

    }

    private void validateSeats(SeatHold seatHold, Venue venue, Status status) {
        seatHold.getSeatsHeldOrReserved().entrySet().forEach(entry -> {
            venue.getVenueMap().get(new Row(entry.getKey()))
                    .subList(Integer.parseInt(entry.getValue().split(Constants.delimiter)[0]) - 1,
                            Integer.parseInt(entry.getValue().split(Constants.delimiter)[1]) - 1).stream().forEach(venueEntry -> {
                Assert.assertTrue("Status must be HELD", venueEntry.getStatus() == status);
            });
        });
    }
}
