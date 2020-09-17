package lesson1;


public class ThreadJoinExample {
    public static void main(String[] args) {
        Integer[] values = new Integer[] { 3, 1, 14, 3, 4, 5, 6, 7, 8, 9, 11, 3, 2, 1 };
        
        // Average is a task that implements Runnable
        Average avg = new Average(values);

        // Median is a task that implements Runnable
        Median median = new Median(values);

        Thread t1 = new Thread(avg, "t1");
        Thread t2 = new Thread(median, "t2");

        System.out.println("Start the thread t1 to calculate average");
        t1.start();
        
        System.out.println("Start the thread t2 to calculate median");
        t2.start();

        try { 
            System.out.println("Join on t1");
            t1.join();
            System.out.println("t1 has done with its job of calculating average");
        } catch (InterruptedException e) {
            System.out.println(t1.getName() + " interrupted");
        }

        try {
            System.out.println("Join on t2");
            t2.join();
            System.out.println("t2 has done with its job of calculating median");
        } catch (InterruptedException e) {
            System.out.println(t2.getName() + " interrupted");
        }

        System.out.println("Average: " + avg.getMean() + ", Median: " + median.getMedian());
    }
}