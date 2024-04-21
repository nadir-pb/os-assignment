import PageTable.PageTable;
import algorithms.MainLogic;
import algorithms.PhysicalMemory;

import cache.Tlb;
import util.Helper;
import util.Reader;

public class Main {
    private static final Reader addressReader = new Reader();
    private static final PageTable pageTable = new PageTable();
    private static final Tlb tlb = new Tlb();
    private static final PhysicalMemory physicalMemory = new PhysicalMemory();
    private static final Helper helper = new Helper();
    private static final MainLogic mainLogic = new MainLogic();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to the Virtual Memory Simulator!");
        Thread.sleep(500);
        System.out.println("In progress");

        MainLogic.processAddresses(addressReader, tlb, helper, physicalMemory, pageTable);
        Thread.sleep(1000);
        System.out.println("End of  program");
    }
}