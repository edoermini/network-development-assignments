package com.bankaccount.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Utils {

    public static Path openFile(String fileName) {
        Path path = Paths.get(fileName);
        Path file = null;

        // creates file if not exists
        if (Files.notExists(path)) {
            try {
                file = Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileChannel.open(path, StandardOpenOption.WRITE).truncate(0).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = path;
        }

        return file;
    }
}
