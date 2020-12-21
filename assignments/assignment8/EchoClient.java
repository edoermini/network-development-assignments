import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {

        if (args.length < 2 || args.length > 2) {
            System.out.println("Usage: EchoClient host port");
            System.out.println("Exiting...");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        SocketChannel client = null;

        System.out.println("[?] Type exit to quit\n");

        // creating socket channel
        try {
            client = SocketChannel.open(new InetSocketAddress(host, port));
            client.configureBlocking(true);
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            Scanner sc = new Scanner(System.in);
            String line = "";

            while (!line.equals("exit")) {
                System.out.print("> ");
                line = sc.nextLine();

                if (line.length() == 0 || line.trim().equals("")) {
                    continue;
                }

                
                buffer.put(line.getBytes());
                buffer.flip();

                client.write(buffer);

                buffer.clear();

                client.read(buffer);
                
                String response = new String(buffer.array()).trim();
                
                System.out.println(response);
                
                buffer = ByteBuffer.allocate(1024);

            }

            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
