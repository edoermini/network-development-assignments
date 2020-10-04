package assignment2;

import java.security.InvalidParameterException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Edoardo Ermini
 */
public class PostalOffice {
    private ThreadPoolExecutor offices;

    public PostalOffice(Integer k) throws InvalidParameterException {
        
        if (k < 0) {
            throw new InvalidParameterException("Queue dimention parameter must be a positive value");
        }

        this.offices = new ThreadPoolExecutor(
            4,          // core pool dimention 
            4,          // max pool dimention

            // threads removed after 5s of inactivity
            0,
            TimeUnit.SECONDS,

            // k-4 Peole inside queue and 4 being served
            new ArrayBlockingQueue<Runnable>(k-4)
        );
    }

    public void addPerson(Person p) {

        /**
         * If pool queue is full will be called insert method
         * that calls the blocking method put.
         * 
         * (this avoids busy-wait)
         */
        if (this.isFull()) {
                
            try {
                this.insert(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        
        } else {
            offices.execute(p);
        }
    }

    public void closeOffice() {
        offices.shutdown();
    }

    private boolean isFull() {
        return (this.offices.getQueue().remainingCapacity() == 0 ) ? true : false;
    }

    private void insert(Person p) throws InterruptedException {
        this.offices.getQueue().put(p);
    }

    public static void main(String[] args) {

        if (args.length < 2 || args.length > 2) {
            System.out.println("Usage: PostalOffice first_queue_dim second_queue_dim");
            return;
        }

        if (Integer.valueOf(args[1]) <= 4) {
            System.out.println("Error: second_queue_dim must be greater or equal to 4");
            System.out.println("Exiting...");
            return;
        }
        
        Integer firstQDim = Integer.valueOf(args[0]);
        Integer secondQDim = Integer.valueOf(args[1]);

        ArrayBlockingQueue<Person> firstQ = new ArrayBlockingQueue<Person>(firstQDim);
        PostalOffice office = new PostalOffice(secondQDim);

        for (int i = 0; i < firstQDim; i++) {
            Person p = new Person(i);
            firstQ.add(p);
        }

        for (Person p : firstQ) {
            office.addPerson(p);
        }

        office.closeOffice();
    }
}
