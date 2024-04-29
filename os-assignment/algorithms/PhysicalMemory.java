package algorithms;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import util.Constants;

public class PhysicalMemory {

    private byte[][] memory = new byte[Constants.RAM_SIZE][Constants.RAM_SIZE];
    private boolean[] secondChanceBits = new boolean[Constants.RAM_SIZE];
    private int nextAvailableFrameNum = 0;

    public PhysicalMemory() {
        for (int i = 0; i < Constants.RAM_SIZE; i++) {
            memory[i][0] = -1;
            memory[i][1] = -1;
            secondChanceBits[i] = false;
        }
    }

    public byte[][] getMemory() {
        return this.memory;
    }

    public int loadFrameFromDiskAndStore(int pageNumber, int frameIndex) {
        File backingStoreFile = new File("files/disk_sim");
        if (!backingStoreFile.exists()) {
            System.out.println("BACKING_STORE file not found in application directory!");
            return -1;
        }
        try (RandomAccessFile randomAccess = new RandomAccessFile(backingStoreFile, "r")) {
            randomAccess.seek(pageNumber * Constants.RAM_SIZE);
            for (int i = 0; i < Constants.RAM_SIZE; i++) {
                memory[frameIndex][i] = randomAccess.readByte();
            }
            secondChanceBits[frameIndex] = false; 
            return frameIndex;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isMemoryFull(){
        return nextAvailableFrameNum == Constants.RAM_SIZE;
    }

    public int loadFrameFromDiskAndStore(int pageNumber) {
        File backingStoreFile = new File("files/disk_sim");
        if (!backingStoreFile.exists()) {
            System.out.println("BACKING_STORE file not found in application directory!");
            return -1;
        }
        try (RandomAccessFile randomAccess = new RandomAccessFile(backingStoreFile, "r")) {
            randomAccess.seek(pageNumber * 256);
            for (int i = 0; i < 256; i++) {
                memory[nextAvailableFrameNum][i] = randomAccess.readByte();
            }
            secondChanceBits[nextAvailableFrameNum] = true;
            nextAvailableFrameNum++;
            return (nextAvailableFrameNum - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int replaceFrame(int pageNumber) {
        int victimFrame = findVictimFrame();
        if (victimFrame != -1) {
            return loadFrameFromDiskAndStore(pageNumber, victimFrame);
        } else {
            return -1;
        }
    }

    private int findVictimFrame() {
        int victimFrame = -1;
        while (victimFrame == -1) {
            for (int i = 0; i < secondChanceBits.length; i++) {
                if (!secondChanceBits[i]) {
                    victimFrame = i;
                    break;
                } else {
                    // Reset the second chance bit for the current frame to false
                    secondChanceBits[i] = false;
                }
            }
        }
        return victimFrame;
    }
}
