package codingchallenge.constants;

/**
 *  Maintains the default values for the system
 */
public class Constants {
    public static final String delimiter = "-";
    public static final int TIMEOUT = 10;
    public static final int ROWS = 10;
    public static final int SEATSPERROW = 10;
    public static final int SERVERPORT = 9999;

    public static int getNumSeatsHeldOrReserved(String value) {
        String[] values = value.split(Constants.delimiter);
        return Integer.parseInt(values[1]) - Integer.parseInt(values[0]) + 1;
    }
}