package codingchallenge;


import codingchallenge.handlers.ClientHandler;
import org.apache.log4j.Logger;

/**
 *  Initializes and Spawns multiple client processes
 */

public class ClientInit {

    final static Logger logger = Logger.getLogger(ClientInit.class);

    public static void main(String[] args) throws Exception {
        try {
            if(args.length != 2) {
                logger.error("Usage: java ClientInit serverhost serverport");
                return;
            }else {
                ClientHandler client = new ClientHandler(args[0], Integer.parseInt(args[1]));
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
