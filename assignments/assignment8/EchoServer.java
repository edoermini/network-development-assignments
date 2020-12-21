import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

class EchoServer {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 1) {
            System.out.println("Usage: EchoServer port");
            System.out.println("Exiting...");
            return;
        }

        int port = Integer.parseInt(args[0]);
        ServerSocketChannel server = null;
        Selector selector = null;

        // creating socket channel
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creating selector

        try {
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[+] Server is listening on port " + port);

        while (true) {

            try {

                // filling selected set
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            } 

            // getting selected set
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    
            // getting iterator
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            // multiplexing 
            while (keyIterator.hasNext()) {
                SelectionKey key = (SelectionKey) keyIterator.next();

                // removes key from selected set but not from registered set
                keyIterator.remove();

                try {
                    if (key.isAcceptable()) { // Accept

                        register(key, selector);

                    } else if (key.isReadable()) { // Read

                        read(key, selector);
                    } else if (key.isWritable()) { // Write

                        answer(key, selector);
                    }

                } catch (IOException e) {
                    key.cancel();

                    try {
                        key.channel().close();
                    } catch (IOException ce) {
                        ce.printStackTrace();
                    }
                }
            } 
        }
    }

    private static void read(SelectionKey key, Selector selector) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buff = (ByteBuffer) key.attachment();

        buff.put("echoed by server: ".getBytes());
        client.read(buff);
        buff.flip();

        client.register(selector, SelectionKey.OP_WRITE, buff);
    }

    private static void answer(SelectionKey key, Selector selector) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buff = (ByteBuffer) key.attachment();

        // getting message sent by client without "echoed by server" part
        String message = new String(buff.array()).trim().split(":")[1].trim();

        client.write(buff);
        buff.clear();

        if (message.equals("exit")) {
            System.out.println("[+] Connection with client " + client.getRemoteAddress() + " closed");
            client.close();
            key.cancel();

            return;
        }

        buff = ByteBuffer.allocate(1024);

        client.register(selector, SelectionKey.OP_READ, buff);

    }

    private static void register (SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel srv = (ServerSocketChannel) key.channel();
        SocketChannel client;

        client = srv.accept();
        client.configureBlocking(false);

        ByteBuffer buff = ByteBuffer.allocate(1024);

        client.register(selector, SelectionKey.OP_READ, buff);

        System.out.println("[+] Connection with client " + client.getRemoteAddress() + " started");
    }
}