package com.bankaccount;

import com.bankaccount.elements.BankAccount;
import com.bankaccount.elements.Purpose;
import com.bankaccount.io.Reader;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReaderThread implements Runnable {
    private final ExecutorService pool;
    private final HashMap<Purpose, Integer> freq;
    private final String fileName;

    public ReaderThread(HashMap<Purpose, Integer> freq, String fileName) {
        this.pool = Executors.newCachedThreadPool();
        this.freq = freq;
        this.fileName = fileName;
    }

    public void run() {
        List<BankAccount> bas = Reader.jsonFile2BAs(fileName);

        for (BankAccount ba : bas) {
            pool.execute(new AnalyzerThread(freq, ba));
        }

        pool.shutdown();

        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
