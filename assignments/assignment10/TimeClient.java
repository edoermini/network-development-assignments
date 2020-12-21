import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * @author Edoardo Ermini
 */
public class TimeClient {
    public static void main(String[] args) {

        if (args.length < 2 || args.length > 2) {
            System.out.println("Usage TimeClient dategroup port");
            System.out.println("Exiting...");
            return;
        }

        int port;
        String host = args[0];
        InetAddress dategroup = null;
        MulticastSocket ms = null;

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error on dategroup argument");
            System.out.println("Exiting...");
            return;
        }

        try {
            dategroup = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            System.out.println("Error on dategroup argument");
            System.out.println("Exiting...");
            return;
        }

        try {
            ms = new MulticastSocket(port);
            ms.joinGroup(dategroup);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        byte[] buf = new byte[19];

        for (int i = 0; i < 10; i++) {
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            
            try {
                ms.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = new String(dp.getData());
            System.out.println(s);
        }
        
    }
}
