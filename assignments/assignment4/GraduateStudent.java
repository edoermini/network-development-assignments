package assignment4;

/**
 * @author Edoardo Ermini
 */
public class GraduateStudent implements Runnable {
    Tutor lab;
    int k;
    int pc;

    public GraduateStudent(Tutor t) {
        this.lab = t;
        this.k = (int)(Math.random() * 10);

        /**
         * choosing lab pc from 1 to 20
         */
        this.pc = (int)(Math.random() * 20 + 1);
    }

    public void run() {

        for (int i = 0; i < this.k; i++) {

            System.out.println("(" + Thread.currentThread().getName() + ")\t" + "User: GraduateStudent\t" + " is trying to get access to the lab");

            lab.getAccess(this.pc);

            System.out.println("(" + Thread.currentThread().getName() + ")\t" + "User: GraduateStudent\t" + " got access to the lab");
            
            /**
             * simulates the time spent on pc
             */
            try {
                Thread.sleep((int)(Math.random()*5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("(" + Thread.currentThread().getName() + ")\t" + "User: GraduateStudent\t" + " is leaving the lab");

            lab.finished(this.pc);

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
