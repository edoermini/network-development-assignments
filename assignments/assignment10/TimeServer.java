import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Edoardo Ermini
 */
class TimeServer {
    String dategroup;
    int port;

    public TimeServer(String dategroup, int port) {
        this.dategroup = dategroup;
        this.port = port;
    }

    public void serve() {
        InetAddress ia = null;
        DatagramSocket ms = null;

        try {
            ia = InetAddress.getByName(this.dategroup);
        } catch (UnknownHostException e) {
            System.out.println("Usage TimeServer dategroup port");
            System.out.println("Exiting...");
            return;
        }

        try {
            ms = new DatagramSocket(this.port);
            ms.setReuseAddress(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(("yyyy/MM/dd HH:mm:ss"));
            byte[] buf = dtf.format(LocalDateTime.now()).getBytes();

            DatagramPacket dp = new DatagramPacket(buf, buf.length, ia, port);

            try {
                ms.send(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 2) {
            System.out.println("Usage TimeServer dategroup port");
            System.out.println("Exiting...");
            return;
        }

        int port;
        String host = args[0];

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error on dategroup argument");
            System.out.println("Exiting...");
            return;
        }

        TimeServer server = new TimeServer(host, port);

        server.serve();
    }


}