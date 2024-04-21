package algorithms;

import PageTable.PageTable;
import cache.Tlb;
import util.Helper;
import util.Reader;

public class MainLogic {

    private static final Logger logger = new Logger("C:\\Users\\ASUS\\Desktop\\os-assignment\\os-assignment\\files\\log.txt");

    public static void processAddresses(Reader addressReader, Tlb tlb, Helper helper,
                                        PhysicalMemory physicalMemory, PageTable pageTable) {

        int logicalAddress, physicalAddress, pageNumber, offset, frameNumber;
        int[] pageNumberAndOffset;
        byte value;
        int numPageFaults = 0, numTLBHits = 0;

        while (true) {
            logicalAddress = addressReader.getNextAddress();
            if (logicalAddress == -1000000) {
                break; // reached end of addresses
            }
            pageNumberAndOffset = getPageNumberAndOffsetFromAddress(logicalAddress);
            pageNumber = pageNumberAndOffset[0];
            offset = pageNumberAndOffset[1];

            if (tlb.doesPageExist(pageNumber)) {

                // this page already exists in the tlb lookup table, thus it is already loaded in physical memory (no page fault)
                frameNumber = tlb.getFrameNumber(pageNumber);
                physicalAddress = helper.getPhysicalAddress(frameNumber, offset);
                value = helper.getByteFromMemory(frameNumber, offset, physicalMemory.getMemory());
                numTLBHits++;

            } else if (pageTable.doesFrameExist(pageNumber)) {

                // this page already exists in the page table, thus it is already loaded in physical memory (no page fault)
                frameNumber = pageTable.getFrameNumber(pageNumber);
                tlb.setFrameNumber(pageNumber, frameNumber);
                physicalAddress = helper.getPhysicalAddress(frameNumber, offset);
                value = helper.getByteFromMemory(frameNumber, offset, physicalMemory.getMemory());

            } else {

                // this page is not in the page table or tlb lookup table, thus is is not loaded in memory (Page fault)
                // load it into memory and store the returned frame in the page table and tlb with the associated page number
                numPageFaults++;
                frameNumber = physicalMemory.loadFrameFromDiskAndStore(pageNumber);

                if (frameNumber == -1) {
                    return; // terminate execution
                }
                pageTable.setFrameNumber(pageNumber, frameNumber);
                tlb.setFrameNumber(pageNumber, frameNumber);
                physicalAddress = helper.getPhysicalAddress(frameNumber, offset);
                value = helper.getByteFromMemory(frameNumber, offset, physicalMemory.getMemory());
            }
            logger.log(String.format("Virtual Address: %d\nPhysical Address: %d\nValue: %d",
                    logicalAddress, physicalAddress, value));

        }
        logger.log(String.format("Number of page faults: %d/1000 (%.2f%%)", numPageFaults, numPageFaults / 1000.0 * 100));
        logger.log(String.format("TLB hit rate: %d/1000 (%.2f%%)", numTLBHits, numTLBHits / 1000.0 * 100));
    }

    public static int[] getPageNumberAndOffsetFromAddress(int address) {
        int[] pageNumberAndOffset = new int[2];
        String binary = intToBinary(address);
        if (binary.length() < 16) {
            binary = padBinaryStringTo16Bits(binary); // pad binary to 16 bits
        }
        pageNumberAndOffset[1] = binaryToInt(binary.substring(binary.length() - 8));
        pageNumberAndOffset[0] = binaryToInt(binary.substring(binary.length() - 16, binary.length() - 8));

        return pageNumberAndOffset;
    }

    public static String padBinaryStringTo16Bits(String binary) {
        StringBuilder binaryBuilder = new StringBuilder(binary);
        while (binaryBuilder.length() < 16) {
            binaryBuilder.insert(0, "0");
        }
        binary = binaryBuilder.toString();
        return binary;
    }

    public static String intToBinary(int num) {
        return Integer.toBinaryString(num);
    }

    public static int binaryToInt(String string) {
        int integer = 0;
        int multiplier = 1;
        for (int i = string.length() - 1; i >= 0; i--) {
            if (string.charAt(i) == '1') {
                integer += multiplier;
            }
            multiplier = multiplier * 2;
        }
        return integer;
    }
}
