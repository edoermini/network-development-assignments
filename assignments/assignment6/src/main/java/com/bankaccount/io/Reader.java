package com.bankaccount.io;

import com.bankaccount.elements.BankAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.List;

public class Reader {

    public static List<BankAccount> jsonFile2BAs(String fileName) {
        List<BankAccount> bas = null;
        String basListString = readFile(fileName);

        try {
            bas = Arrays.asList(new ObjectMapper().readValue(basListString, BankAccount[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return bas;
    }

    private static String readFile (String fileName) {
        StringBuilder builder = new StringBuilder();
        ReadableByteChannel src = null;

        File file = new File(fileName);

        // creating read channel
        try {
            src = Channels.newChannel(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creating buffer with same dimension as file
        ByteBuffer buffer = ByteBuffer.allocateDirect((int)file.length());

        // reading to buffer
        try {
            src.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // sets position to 0
        buffer.compact();

        while(buffer.hasRemaining()) {
            builder.append((char)buffer.get());
        }

        return builder.toString();
    }
}
