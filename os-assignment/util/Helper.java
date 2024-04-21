package util;

public class Helper {

    public int getPhysicalAddress(int frameNumber, int offset) {
        return frameNumber * 256 + offset;
    }

    public byte getByteFromMemory(int frameNumber, int offset, byte[][] memory) {
        if (frameNumber < 0 || frameNumber > 255
                || offset < 0 || offset > 255) {
            return -1;
        } else if (isFrameEmpty(frameNumber, memory)) {
            return -1;
        }
        return memory[frameNumber][offset];
    }


    public boolean isFrameEmpty(int frameNumber, byte[][] memory) {
        if (frameNumber < 0 || frameNumber > 255) {
            return false;
        }
        return memory[frameNumber][0] == -1
                && memory[frameNumber][1] == -1;
    }
}