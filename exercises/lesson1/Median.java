package lesson1;

public class Median implements Runnable {
    Integer[] values;
    Float median;

    public Median(Integer[] values) {
        this.values = new Integer[values.length];
        this.median = Float.valueOf(0);

        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    public String getMedian() {
        return median.toString();
    }

    public void run() {
        
        if (this.values.length % 2 != 0) {
            int index = (this.values.length + 1) / 2;
            
            this.median = Float.valueOf(this.values[index]);
        } else {
            int first = ((this.values.length + 1) / 2)-1;
            int second = first + 1;
            
            this.median = Float.valueOf(this.values[first] + this.values[second]) / 2;
        }
    }
}
