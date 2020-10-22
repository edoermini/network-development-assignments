package assignment5;

import java.io.File;
import java.util.LinkedList;

public class Consumer implements Runnable {
    private LinkedList<String> queue;
    private String finishMessage;

    public Consumer(LinkedList<String> queue) {
        this.queue = queue;
        this.finishMessage = "FINISH";
    }

    public void run() {

        String dirname;
        File dir;

        while (true) {
        
            synchronized(queue) {
                while(queue.size() == 0) {

                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                dirname = queue.pop();
            

                if (dirname.equals(this.finishMessage)) {
                    queue.add(this.finishMessage);
                    return;
                }

                System.out.println();
                System.out.println("==================> " + dirname);

                dir = new File(dirname);

                /**
                 * This listing is within a synchronized block to 
                 * avoid the interleaving of prints of files in other
                 * directories
                 */
                for (File f : dir.listFiles()) {
                    if (f.isDirectory()) {
                        System.out.println("Dir:\t" + f.getName());
                    } else if (f.isFile()) {
                        System.out.println("File:\t" + f.getName());
                    }
                }

            }
        }
    }
}
