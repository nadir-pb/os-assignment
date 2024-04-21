package algorithms;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PhysicalMemory {

    private final byte[][] memory = new byte[256][256];
    private int nextAvailableFrameNum = 0;

    public PhysicalMemory() {

        for (int i = 0; i < 256; i++) {
            memory[i][0] = -1;
            memory[i][1] = -1;
        }
    }

    public byte[][] getMemory() {
        return this.memory;
    }

    public int loadFrameFromDiskAndStore(int pageNumber) {
        File backingStoreFile = new File("C:\\Users\\ASUS\\Desktop\\os-assignment\\os-assignment\\files\\disk_sim");

        if (!backingStoreFile.exists()) {
            System.out.println("BACKING_STORE file not found in application directory!");
            return -1;
        }
        RandomAccessFile randomAccess = null;

        try {
            randomAccess = new RandomAccessFile(backingStoreFile, "r");
            randomAccess.seek(pageNumber * 256L);


            for (int i = 0; i < 256; i++) {
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