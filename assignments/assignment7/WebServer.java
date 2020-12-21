package assignment7;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    public static void main(String[] args) throws Exception {

        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage WebServer port webserver_root");
            System.out.println("Exiting...");
            return;
        }

        Integer port = Integer.valueOf(args[0]);
        String root = args[1];

        ExecutorService pool = Executors.newFixedThreadPool(20);

        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("[+] The web server is listening on port " + port);

            while(true) {
                pool.execute(new Server(listener.accept(), root));
            }
        }

    }
}
