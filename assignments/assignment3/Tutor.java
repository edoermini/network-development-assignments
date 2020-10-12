package assignment3;

import java.security.InvalidParameterException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

/**
 * @author Edoardo Ermini
 */
public class Tutor {

    /**
     * computers is an abstraction of real PCs
     * for each i from 0 to labPCs.size()-1
     *  labPCs.get(i) == true   if pc i is used by someone,
     *  labPCs.get(i) == false  if ps i is free
     */
    private ArrayList<Boolean> computers;

    private ReentrantLock labDoor;
    private Condition freePC;
    
    /**
     * Counts how many teachers are waiting
     */
    private Integer teachersWaiting;

    /**
     * Counts how many graduate students are waiting
     */
    private Integer graduatesWaiting;

    /**
     * Counts how many students are waiting
     */
    private Integer studentsWaiting;


    public Tutor() {
        this.computers = new ArrayList<Boolean>(20);

        /**
         * initializing all Pc to false (free)
         */
        for (int i = 0; i < 20; i++) {
            this.computers.add(false);
        }

        this.labDoor = new ReentrantLock();
        this.freePC = labDoor.newCondition();
    
        this.teachersWaiting = 0;
        this.graduatesWaiting = 0;
        this.studentsWaiting = 0;
    }

    /**
     * Method for Student users that blocks until a pc is free and 
     * there isn't any Teacher or Graduate user waiting for access
     * and after gives the access to user
     * 
     * @return          the assigned pc (integer from 1 to 20)
     */
    public int getAccess() {
        
        int pc = 0;

        labDoor.lock();

        studentsWaiting += 1;

        try {
            while (
                teachersWaiting > 0 || 
                graduatesWaiting > 0 || 
                isAllOccupied()
            ) {
                freePC.await();
            }

            studentsWaiting -= 1;

            pc = getFreePC();
            setBusy(pc);
            
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            labDoor.unlock();
        }

        return pc+1;
    }

    /**
     * Method for Graduate users that blocks until given pc is free and 
     * there isn't any Teacher user waiting for access
     * and after gives the access to user
     * 
     * @param   pc      pc that user want to use (integer from 1 to 20)
     * @throws InvalidParameterException if pc is lower than 1 or greather than 20
     */
    public void getAccess(int pc) throws InvalidParameterException {

        if (pc < 1 || pc > 20) {
            throw new InvalidParameterException("pc must be between 1 and 20");
        }

        labDoor.lock();

        graduatesWaiting += 1;

        try {
            while (
                teachersWaiting > 0 ||  
                isOccupied(pc-1)
            ) {
                freePC.await();
            }

            graduatesWaiting -= 1;

            setBusy(pc-1);
            
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            labDoor.unlock();
        } 
    }

    /**
     * Method for Teacher users that blocks until all lab PCs are free 
     * and after gives the access to user
     */
    public void getLabAccess() {

        labDoor.lock();

        teachersWaiting += 1;

        try {
            while (isAllOccupied()) {
                freePC.await();
            }

            teachersWaiting -= 1;

            setAllBusy();
            
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            labDoor.unlock();
        } 
    }

    /**
     * Method called by Graduate or Student users to inform Tutor that
     * the pc they used is now free
     * 
     * @param pc the pc that is now free
     * @throws InvalidParameterExeption if pc is lower than 1 or greather than 20
     */
    public void finished(int pc) throws InvalidParameterException {

        if (pc < 1 || pc > 20) {
            throw new InvalidParameterException("pc must be between 1 and 20");
        }

        labDoor.lock();

        setFree(pc-1);
        
        freePC.signalAll();
        labDoor.unlock();
    }

    /**
     * Method called by Teacher users to inform Tutor that
     * the whole lab is free
     */
    public void finished() {
        labDoor.lock();

        setAllFree();

        freePC.signalAll();
        labDoor.unlock();
    }

    private boolean isAllOccupied() {
        int occupiedPCs = 0;

        labDoor.lock();
        for (Boolean pc : this.computers) {
            occupiedPCs += (pc ? 1 : 0);
        }
        labDoor.unlock();

        if (occupiedPCs == this.computers.size()) 
            return true;
        
        return false;
    }

    private boolean isOccupied(int pc) {
        return this.computers.get(pc);
    }

    private int getFreePC() {
        int pc = 0;

        for (int i = 0; i < this.computers.size(); i++) {
            if (!computers.get(i)) 
                pc = i;
                break; 
        }

        return pc;
    }

    private void setBusy(int pc) {
        this.computers.set(pc, true);
    }

    private void setFree(int pc) {
        this.computers.set(pc, false);
    }

    private void setAllBusy() {
        for (int i = 0; i < this.computers.size(); i++) {
            computers.set(i, true);
        }
    }

    private void setAllFree() {
        for (int i = 0; i < this.computers.size(); i++) {
            computers.set(i, false);
        }
    }


    public static void main(String[] args) {
        
        if (args.length < 3 || args.length > 3){
            System.out.println("Usage: Tutor n_students n_graduates n_teachers");
            System.out.println("Exiting...");
            return;
        }

        Integer n_students = Integer.valueOf(args[0]);
        Integer n_graduates = Integer.valueOf(args[1]);
        Integer n_teachers = Integer.valueOf(args[2]);

        Tutor lab = new Tutor();

        for (int i = 0; i < n_students; i++) {
            Thread t = new Thread(new Student(lab));
            t.start();
        }

        for (int i = 0; i < n_graduates; i++) {
            Thread t = new Thread(new GraduateStudent(lab));
            t.start();
        }

        for (int i = 0; i < n_teachers; i++) {
            Thread t = new Thread(new Teacher(lab));
            t.start();
        }
    }
}
