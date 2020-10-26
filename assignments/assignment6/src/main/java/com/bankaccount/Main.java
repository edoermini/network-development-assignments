package com.bankaccount;

import com.bankaccount.elements.BankAccount;
import com.bankaccount.elements.Purpose;
import com.bankaccount.generator.Generator;
import com.bankaccount.io.Writer;

import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage Main filename.json");
            System.out.println("Exiting...");
            return;
        }

        String fileName = args[0];
        HashMap<Purpose, Integer> freq = new HashMap<>();

        // Initializing freq
        for (Purpose p: Purpose.values()) {
            freq.put(p, 0);
        }



        /* Bank accounts generation and serialization SECTION */

        System.out.println("[+] Generating bank account objects");
        int nBankAccounts = (int) (Math.random() * 50) + 1;
        List<BankAccount> bas = Generator.generateBankAccounts(nBankAccounts, 1000, 3000);

        System.out.println("[+] Serializing bank account objects to " + fileName + " file...");
        Writer.bas2JSONFile(bas, fileName);



        /* Purposes frequencies calculation SECTION */

        System.out.println("[+] Calculating purposes frequencies...");

        Thread reader = new Thread(new ReaderThread(freq, fileName));
        reader.start();

        try {
            reader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();

        for (Purpose p : Purpose.values()) {
            System.out.println("The frequency of " + p + " is " + freq.get(p));
        }
    }
}
