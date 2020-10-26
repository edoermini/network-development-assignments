package com.bankaccount;

import com.bankaccount.elements.BankAccount;
import com.bankaccount.elements.Purpose;

import java.util.HashMap;
import java.util.Map;

public class AnalyzerThread implements Runnable {
    private final HashMap<Purpose, Integer> freq;
    private final BankAccount ba;

    public AnalyzerThread(HashMap<Purpose, Integer> freq, BankAccount ba) {
        this.freq = freq;
        this.ba = ba;
    }

    public void run() {
        Map<Purpose, Integer> baFreq = ba.getPurposesFrequency();

        for (Purpose p : Purpose.values()) {
            synchronized (freq) {
                freq.replace(p, freq.get(p) + baFreq.get(p));
            }
        }
    }
}
