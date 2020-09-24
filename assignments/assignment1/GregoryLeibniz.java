import java.security.InvalidParameterException;

/**
 * @author Edoardo Ermini
 */
public class GregoryLeibniz implements Runnable {
    Double pi;
    Double accuracy;

    public GregoryLeibniz(float accuracy) throws InvalidParameterException {

        if (accuracy < 0 || accuracy > Math.PI) {
            throw new InvalidParameterException("accuracy must be between 0 and pi");
        }

        this.pi = Double.valueOf(0);
        this.accuracy = Double.valueOf(accuracy);
    }

    public Double getPi() {
        return this.pi;
    }

    public void run() {
        int sum_factor = 1;
        int n_iter = 0;

        while (!Thread.currentThread().isInterrupted() && Math.abs(this.pi-Math.PI) >= accuracy) {
            this.pi = (double) ((n_iter % 2 == 0)   ? 
                this.pi + 4.0 / sum_factor          :
                this.pi - 4.0 / sum_factor)         ;
            
            n_iter++;
            sum_factor += 2;
        }
    }

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: GregoryLeibniz accuracy timeout(ms)");
            return;
        }

        Float accuracy = Float.parseFloat(args[0]);
        Integer timeout = Integer.parseInt(args[1]);

        if (accuracy < 0 || accuracy > Math.PI) {
            System.out.println("Accuracy must be between 0 and pi");
            System.out.println("Exiting...");
            return;
        }

        GregoryLeibniz gl = new GregoryLeibniz(accuracy);

        Thread t = new Thread(gl);
        t.start();

        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            System.out.println(e);
            System.exit(1);
        }

        System.out.println("[+] Got timeout");

        if (t.isAlive()) {
            t.interrupt();
            System.out.println("[!] Interrupting thread");
        }

        System.out.println("PI calculated: " + gl.getPi().toString());
    }
}