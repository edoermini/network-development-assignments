package com.bankaccount.generator;

import com.bankaccount.elements.BankAccount;
import com.bankaccount.elements.Movement;
import com.bankaccount.elements.Purpose;
import com.bankaccount.generator.namesgen.Country;
import com.bankaccount.generator.namesgen.NameGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Edoardo Ermini
 */
public class Generator {

    /**
     * Generates a list of BankAccounts
     *
     * @param nBankAccounts integer greater than 0
     * @param minMovements  integer greater or equal than 0
     * @param maxMovements  integer greater than minMovements
     * @return the list of generates BankAccounts objects
     * @throws IllegalArgumentException if nBankAccounts <= 0 || minMovement < 0 || maxMovement < minMovement
     */
    public static List<BankAccount> generateBankAccounts(int nBankAccounts, int minMovements, int maxMovements)
            throws IllegalArgumentException {
        if (nBankAccounts <= 0) {
            throw new IllegalArgumentException("nBankAccounts must be greater than 0");
        }

        if (minMovements < 0) {
            throw new IllegalArgumentException("minMovements must be greater than 0");
        }

        if (maxMovements < minMovements) {
            throw new IllegalArgumentException("maxMovements must be greater than minMovements");
        }

        ArrayList<BankAccount> bas = new ArrayList<>();

        // Generating nBankAccounts Java objects
        for (int i = 0; i < nBankAccounts; i++) {
            BankAccount ba = generateBankAccount(minMovements, maxMovements);
            bas.add(ba);
        }

        return bas;
    }

    /**
     * Generates a BankAccount
     *
     * @param minMovements integer greater or equal than 0
     * @param maxMovements integer greater than minMovements
     * @return the generated bank account
     * @throws IllegalArgumentException if minMovements < 0 || maxMovements < minMovements
     */
    public static BankAccount generateBankAccount(int minMovements, int maxMovements) throws IllegalArgumentException {
        if (minMovements < 0) {
            throw new IllegalArgumentException("minMovements must be greater than 0");
        }

        if (maxMovements < minMovements) {
            throw new IllegalArgumentException("maxMovements must be greater than minMovements");
        }

        // Generates the bank account owner's name
        String name = new NameGenerator().generate(Country.Italy);

        BankAccount ba = new BankAccount(name);

        // Generates a random number of movements from maxMovements to minMovements
        int nMovements = (int) (Math.random() * maxMovements) + minMovements;

        // Adding nMovements to ba object
        for (int j = 0; j < nMovements; j++) {
            ba.addMovement(generateMovement(LocalDate.now().minusYears(2), LocalDate.now()));
        }

        return ba;
    }

    /**
     * Generates a movement with random date in a range and purpose
     *
     * @param fromDate indicates the begin of dates range in which take a random date
     * @param toDate indicates the end of dates range in which take a random date
     * @return the generated movement
     */
    public static Movement generateMovement(LocalDate fromDate, LocalDate toDate) {
        LocalDate date = getRandomDate(fromDate, toDate);
        return new Movement(date, Purpose.randomPurpose());
    }

    private static LocalDate getRandomDate(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
