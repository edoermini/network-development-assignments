import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Edoardo Ermini
 */
public class Ping {

    private static double average(List <Integer> lst) {
        Integer sum = 0;

        if(!lst.isEmpty()) {
          for (Integer mark : lst) {
              sum += mark;
          }
          return sum.doubleValue() / lst.size();
        }

        return sum;
      }

    public static void main(String[] args) {
        
        if (args.length < 2 || args.length > 2) {
            System.out.println("Usage Ping host port");
            System.out.println("Exiting...");
            return;
        }
        
        DatagramSocket udpSocket = null;
        int port = 0;
        String hostStr = args[0];
        InetAddress host = null;
        int lost = 0;
        ArrayList<Integer> rtts = new ArrayList<Integer>();

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("ERR -arg 1 (port)");
            System.out.println("Exiting...");
            return;
        }

        try {
            host = InetAddress.getByName(hostStr);
        } catch (UnknownHostException e) {
            System.out.println("ERR -arg 0 (host)");
            System.out.println("Exiting...");
            return;
        }

        try {
            udpSocket = new DatagramSocket();
            udpSocket.setSoTimeout(2000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        long pingStart = System.nanoTime();

        for (int i = 0; i < 10; i++) {
            byte[] sndBuff = ("PING " + i + " " + Instant.now().getEpochSecond()).getBytes();
            byte[] rcvBuff = new byte[4];

            DatagramPacket sndPacket = new DatagramPacket(sndBuff, sndBuff.length, host, port);
            DatagramPacket rcvPacket = new DatagramPacket(rcvBuff, rcvBuff.length);

            long start = System.nanoTime();

            try {
                udpSocket.send(sndPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            try {
                udpSocket.receive(rcvPacket);
            } catch (IOException e) {
                System.out.println(new String(sndBuff) + " RTT: *");
                lost++;
                continue;
            }

            long end = System.nanoTime();

            rtts.add((int)(end-start)/1000000);

            System.out.println(new String(sndBuff) + " RTT: " + (end-start)/1000000 + " ms");
        }

        long pingStop = System.nanoTime();

        System.out.println();

        System.out.println("--- " + hostStr + ":" + port + " ping statistics ---");
        System.out.println("10 packet trasmitted, " + (10-lost) + " received, " + ((float)lost/10)*100 + "% packet loss, time " + (pingStop-pingStart)/1000000 + "ms");
        System.out.printf("round-trip (ms) min/avg/max = %d/%.2f/%d\n", Collections.min(rtts), average(rtts), Collections.max(rtts));

        udpSocket.close();
    }
}
