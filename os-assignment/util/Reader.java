package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
    private final ArrayList<Integer> logicalAddresses = new ArrayList<>();
    private BufferedReader logicalAddressReader;
    private int nextAddress = 0;

    public Reader() {
        setup();
    }

    public void setup() {
        File addressFile = new File(Constants.FILE);

        if (!addressFile.exists()) {
            System.out.println("Addresses.txt file not found in application directory!");
            return;
        }
        try {
            logicalAddressReader = new BufferedReader(new FileReader(addressFile));
            String line;
            while ((line = logicalAddressReader.readLine()) != null) {
                if (!line.equals("")) {
                    logicalAddresses.add(Integer.valueOf(line));
                }
            }
            logicalAddressReader.close();
        } catch (IOException e) {
            e.printStackTrace();

            try {
                logicalAddressReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public int getNextAddress() {
        if (nextAddress < logicalAddresses.size()) {
            int address = logicalAddresses.get(nextAddress);
            nextAddress++;
            return address;
        } else {
            return -1000000;
        }
    }
}