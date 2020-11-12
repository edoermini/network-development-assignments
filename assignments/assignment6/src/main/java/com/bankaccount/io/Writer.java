package com.bankaccount.io;

import com.bankaccount.elements.BankAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.List;

public class Writer {

    public static void bas2JSONFile(List<BankAccount> bas, String fileName) {

        String dataString = null;

        try {
            dataString = new ObjectMapper().writeValueAsString(bas);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // creating an array of bytes from all serialized bank accounts
        byte[] bytes = dataString.getBytes();

        writeToFile(bytes, fileName);

    }

    private static void writeToFile(byte[] bytes, String fileName) {
        WritableByteChannel dest = null;
        int buffDim = bytes.length;

        // allocating a buffer with same dimension as bytes array
        ByteBuffer buffer = ByteBuffer.allocate (buffDim);

        // creating output channel
        try {
            dest = Channels.newChannel(new FileOutputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // writing bytes to buffer
        buffer.put(bytes);

        // sets position to 0
        buffer.compact();

        // writing into file
        try {
            dest.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
