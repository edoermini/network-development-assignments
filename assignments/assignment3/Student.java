package assignment3;

/**
 * @author Edoardo Ermini
 */
public class Student implements Runnable {
    Tutor lab;
    int k;

    public Student(Tutor t) {
        this.lab = t;
        this.k = (int)(Math.random() * 10);
    }

    public void run() {
        int pc;

        for (int i = 0; i < this.k; i++) {

            System.out.println("(" + Thread.currentThread().getName() + ")\t" + "User: Student\t\t" + " is trying to get access to the lab");
            
            pc = lab.getAccess();

            System.out.println("(" + Thread.currentThread().getName() + ")\t" + "User: Student\t\t" + " got access to the lab");
            
            /**
             * simulates the time spent on pc
             */
            try {
                Thread.sleep((int)(Math.random()*5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("(" + Thread.currentThread().getName() + ")\t" + "User: Student\t\t" + " is leaving the lab");

            lab.finished(pc);

            /**
             * simulates the time between two consecutives lab access
             */
            try {
                Thread.sleep((int)(Math.random()*5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
