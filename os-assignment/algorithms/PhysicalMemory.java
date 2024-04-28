package algorithms;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import util.Constants;

public class PhysicalMemory {

    private final byte[][] memory = new byte[Constants.RAM_SIZE][Constants.RAM_SIZE];
    private int nextAvailableFrameNum = 0;

    public PhysicalMemory() {

        for (int i = 0; i < Constants.RAM_SIZE; i++) {
            memory[i][0] = -1;
            memory[i][1] = -1;
        }
    }

    public byte[][] getMemory() {
        return this.memory;
    }

    public int loadFrameFromDiskAndStore(int pageNumber) {
        File backingStoreFile = new File(Constants.BACKING_STORE);

        if (!backingStoreFile.exists()) {
            System.out.println("BACKING_STORE file not found in application directory!");
            return -1;
        }
        RandomAccessFile randomAccess = null;

        try {
            randomAccess = new RandomAccessFile(backingStoreFile, "r");
            randomAccess.seek(pageNumber * 256L);


            for (int i = 0; i < Constants.RAM_SIZE; i++) {
                memory[nextAvailableFrameNum][i] = randomAccess.readByte();
            }

            nextAvailableFrameNum++;

            randomAccess.close();

            return (nextAvailableFrameNum - 1);
        } catch (IOException e) {
            e.printStackTrace();

            try {
                assert randomAccess != null;
                randomAccess.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return -1;
    }
}