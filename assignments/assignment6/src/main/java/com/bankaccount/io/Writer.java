package com.bankaccount.io;

import com.bankaccount.elements.BankAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Writer {

    public static void bas2JSONFile(List<BankAccount> bas, String fileName) {
        Path file = Utils.openFile(fileName);

        for (BankAccount ba : bas) {
            // Serializing BankAccount object to a JSON string
            String serialized = ba2JSONString(ba);

            try {
                Files.write(file, serialized.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String ba2JSONString(BankAccount ba) {
        String serialized = null;

        try {
            serialized = new ObjectMapper().writeValueAsString(ba) + "\n";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return serialized;
    }
}
