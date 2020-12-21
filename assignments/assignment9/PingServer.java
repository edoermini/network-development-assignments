import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

/**
 * @author Edoardo Ermini
 */
public class PingServer {
    private DatagramSocket udpSocket;
    private int port;
 
    public PingServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
    }

    private void serve() throws Exception {
        System.out.println("[+] Running Server at " + InetAddress.getLocalHost());
        
        while (true) {
            
            byte[] rcvBuf = new byte[256];
            DatagramPacket rcvPacket = new DatagramPacket(rcvBuf, rcvBuf.length);

            Random rd = new Random();
            
            udpSocket.receive(rcvPacket);
            
            String msg = new String(rcvPacket.getData()).trim();

            Boolean drop = rd.nextBoolean();

            if (drop) {
                System.out.println(rcvPacket.getAddress() + ":" + rcvPacket.getPort() + "> " + msg + " ACTION: not sent");
                continue;
            }

            int delay = (int)(Math.random()*1000);
            
            System.out.println(rcvPacket.getAddress() + ":" + rcvPacket.getPort() + "> " + msg + " ACTION: delayed " + delay + " ms");

            Thread.sleep(delay);

            byte[] sndBuff = "PONG".getBytes();

            DatagramPacket sndPacket = new DatagramPacket(sndBuff, sndBuff.length, rcvPacket.getAddress(), rcvPacket.getPort());

            udpSocket.send(sndPacket);


        }
    }
    
    public static void main(String[] args) throws Exception {

        if (args.length < 1 || args.length > 1) {
            System.out.println("Usage PingServer port");
            System.out.println("Exiting...");
            return;
        }

        int port = 0;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("ERR -arg 0 (port)");
            System.out.println("Exiting...");
            return;
        }

        PingServer srv = new PingServer(port);
        srv.serve();
    }
}