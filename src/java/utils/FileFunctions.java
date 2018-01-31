/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

/**
 *
 * @author Alejandro
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class FileFunctions {

    public boolean validateFile(String fullPath, boolean create) {
        File file = new File(fullPath);
        if (create && !file.exists()) {
            file.setWritable(true);
            return file.mkdir();
        } else {
            return file.exists();
        }

    }

    public ArrayList<ArrayList<String>> readFileAsAL(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            String fileRow[];
            ArrayList<ArrayList<String>> fileTable = new ArrayList<ArrayList<String>>();
            ArrayList<String> row;
            while (line != null) {
                if (line.length() > 1) {
                    fileRow = line.split("\t");
                    row = new ArrayList<String>();
                    row.addAll(Arrays.asList(fileRow));
                    fileTable.add(row);
                }
                // System.out.println(line);
                line = in.readLine();

            }
            in.close();
            return fileTable;
        } catch (IOException ex) {
            Logger.getLogger(FileFunctions.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}

