package PageTable;

public class PageTable {

    private final int[] pageTable = new int[256];

    public PageTable() {

        for (int i = 0; i < 256; i++) {
            pageTable[i] = -1;
        }
    }

    public boolean doesFrameExist(int pageNumber) {
        if (pageNumber < 0 || pageNumber > 255) {
            return false;
        }
        return pageTable[pageNumber] != -1;
    }

    public int getFrameNumber(int pageNumber) {
        if (pageNumber < 0 || pageNumber > 255) {
            return -1000000;
        }
        if (pageTable[pageNumber] == -1) {
            return -1000000;
        }
        return pageTable[pageNumber];
    }

    public void setFrameNumber(int pageNumber, int frameNumber) {
        if (pageNumber < 0 || pageNumber > 255) {
            return;
        }
        pageTable[pageNumber] = frameNumber;
    }
}
