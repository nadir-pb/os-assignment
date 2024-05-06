public void setFrameNumber(int pageNumber, int frameNumber) {
    int leastRecentlyUsedIndex = 0;
    int minCounterValue = Integer.MAX_VALUE;

    // Check if the page already exists in the TLB
    for (int i = 0; i < Constants.TLB_SIZE; i++) {
        if (tlb[i][0] == pageNumber) {
            tlb[i][1] = frameNumber;  // Update the frame number
            tlb[i][2] = counter++;    // Update the usage counter to mark it recently used
            return;
        }
        // Identify the least recently used entry
        if (tlb[i][2] < minCounterValue) {
            minCounterValue = tlb[i][2];
            leastRecentlyUsedIndex = i;
        }
    }

    // Replace the least recently used entry if the page does not exist
    tlb[leastRecentlyUsedIndex][0] = pageNumber;
    tlb[leastRecentlyUsedIndex][1] = frameNumber;
    tlb[leastRecentlyUsedIndex][2] = counter++;
}