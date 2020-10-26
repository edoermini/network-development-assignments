package com.bankaccount.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edoardo Ermini
 */
public class BankAccount {
    private String name;
    private ArrayList<Movement> movements;

    public BankAccount() {
        this.movements = new ArrayList<>();
    }

    public BankAccount(String name) {
        this.name = name;
        this.movements = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Movement> getMovements() {
        return new ArrayList<>(this.movements);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = new ArrayList<>(movements);
    }

    public void addMovement(Movement movement) {
        this.movements.add(movement);
    }

    /**
     * @return a Map object that indicates for each bank account's movement the purposes frequency
     */
    public Map<Purpose, Integer> getPurposesFrequency() {
        HashMap<Purpose, Integer> frequencies = new HashMap<>();

        // initializing hashmap
        for (Purpose p: Purpose.values()) {
            frequencies.put(p, 0);
        }

        // calculating frequencies
        for (Movement movement : this.movements) {
            Purpose p = movement.getPurpose();
            frequencies.replace(p, frequencies.get(p)+1);
        }

        return frequencies;
    }
}
