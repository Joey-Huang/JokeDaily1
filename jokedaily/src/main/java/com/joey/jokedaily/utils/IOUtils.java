package com.joey.jokedaily.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by joey on 2016/4/30.
 */
public class IOUtils {
    public static String read(String filePath) throws IOException {
        File file = new File(filePath);
        String content = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        while ((content = br.readLine()) != null) {
            sb.append(content + "\n");
        }
        br.close();
        return sb.toString();
    }

    public static void write(String text, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()){
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(text);
        bw.close();
    }
}
