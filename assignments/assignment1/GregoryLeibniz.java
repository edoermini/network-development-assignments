import java.security.InvalidParameterException;

/**
 * Scrivere un programma che attiva un thread T che effettua il calcolo approssimato di pi.
 * Il programma principale riceve in input da linea di comando un parametro
 * che indica il grado di accuratezza (accuracy) per il calcolo di pi 
 * ed il tempo massimo di attesa dopo cui il programma principale interompthread T.
 * Il thread T effettua un ciclo infinito per il calcolo di pi usando la serie di 
 * Gregory-Leibniz ( pi = 4/1 – 4/3 + 4/5 - 4/7 + 4/9 - 4/11 ...).
 * 
 * Il thread esce dal ciclo quando una delle due condizioni seguenti risultaverificata:
 *  1) il thread è stato interrotto
 *  2) la  differenza tra il valore stimato di pi ed il valore Math.PI (della libreria JAVA) è minore di accuracy
 */
public class GregoryLeibniz implements Runnable {
    Double pi;
    Float accuracy;

    public GregoryLeibniz(float accuracy) {

        if (accuracy < 0 || accuracy > Math.PI) {
            throw new InvalidParameterException("accuracy must be between 0 and pi");
        }

        this.pi = Double.valueOf(0);
        this.accuracy = Float.valueOf(accuracy);
    }

    public Double getPi() {
        return this.pi;
    }

    public void run() {
        int sum_factor = 1;
        int n_iter = 0;

        while (!Thread.currentThread().isInterrupted() && Math.abs(this.pi-Math.PI) >= accuracy) {
            this.pi = (double) ((n_iter % 2 == 0) ? this.pi + 4.0 / sum_factor : this.pi - 4.0 / sum_factor);
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