package assignment5;

import java.util.LinkedList;

public class Crawler {
    public static void main(String[] args) {
        
        if (args.length < 2 || args.length > 2) {
            System.out.println("Usage: Crawler n_consumers path/to/dir");
            System.out.println("Exiting...");
            return;
        }

        Integer k = Integer.valueOf(args[0]);
        String dirname = args[1];

        LinkedList<String> queue = new LinkedList<String>();

        /**
         * Starting producer thread
         */
        new Thread(new Productor(queue, dirname)).start();

        /**
         * Starting consumers threads
         */
        for (int i = 0; i < k; i++) {
            new Thread(new Consumer(queue)).start();
        }
    }
}
