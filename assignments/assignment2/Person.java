package assignment2;

import java.security.InvalidParameterException;

/**
 * @author Edoardo Ermini
 */
public class Person implements Runnable {
    public Integer ticketNumber;
    private Integer timeOfService;

    public Person(Integer ticketNumber) throws InvalidParameterException {
        
        if (ticketNumber < 0) {
            throw new InvalidParameterException("Queue dimention parameter must be a positive value");
        }

        this.ticketNumber = ticketNumber;
        this.timeOfService = (int)(Math.random() * 1000);
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void run() {
        System.out.println("Office: " + Thread.currentThread().getName() + " Serving client with ticket " + ticketNumber + "...");

        try {
            Thread.sleep(timeOfService);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
