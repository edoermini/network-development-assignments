package assignment5;

import java.io.File;
import java.util.LinkedList;

import javax.print.event.PrintJobListener;

public class Productor implements Runnable {
    private LinkedList<String> queue;
    private String dirname;
    private String finishMessage;

    public Productor(LinkedList<String> queue, String dirname) {
        this.queue = queue;
        this.dirname = dirname;
        this.finishMessage = "FINISH";
    }

    public void run() {
        File root = new File(this.dirname);

        findDirs(root.getPath());

        synchronized(queue) {
            queue.add(this.finishMessage);
            queue.notifyAll();
        }
    }

    private void findDirs(String dirname) {
        
        synchronized(queue) {
            queue.add(dirname);
            queue.notify();
        }

        File dir = new File(dirname);

        for (File f : dir.listFiles()) {
            
            if (f.isDirectory()) {
                this.findDirs(f.getPath());
            }
        }
    }
}
