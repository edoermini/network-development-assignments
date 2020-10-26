package com.bankaccount.io;

import com.bankaccount.elements.BankAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    public static ArrayList<BankAccount> jsonFile2BAs(String fileName) {
        ArrayList<BankAccount> bas = new ArrayList<>();
        List<String> lines = new ArrayList<>();

        try {
            // read all lines
           lines = Files.readAllLines(Paths.get(fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }

        // deserialize all lines in BankAccount objects and put into bas list
        for (String line : lines) {
            bas.add(jsonString2ba(line));
        }

        return bas;
    }

    public static BankAccount jsonString2ba(String s) {
        BankAccount ba = null;

        try {
            ba = new ObjectMapper().readValue(s, BankAccount.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ba;
    }
}
