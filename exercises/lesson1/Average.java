package lesson1;

public class Average implements Runnable {
    Integer[] values;
    Float avg;

    public Average(Integer[] values) {
        this.values = new Integer[values.length];
        this.avg = Float.valueOf(0);

        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    public String getMean() {
        return avg.toString();
    }

    public void run() {
        int sum = 0;

        for (Integer e : this.values) {
            sum += e;
        }

        this.avg = Float.valueOf(sum) / this.values.length;
    }
}
