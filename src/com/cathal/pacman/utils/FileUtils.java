package com.cathal.pacman.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

    private FileUtils() {

    }

    public static String loadAsString(String filepath) {
        StringBuilder result = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problem loading file: '" + filepath + "'!");
        }

        return result.toString();
    }
}
