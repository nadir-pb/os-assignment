package algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private BufferedWriter writer;

    public Logger(String fileName) {
        try {
            this.writer = new BufferedWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public void log(String message) {
        try {
            writer.write(message + "\n");
            writer.flush(); // Ensures the data is written to the file immediately
        } catch (IOException e) {
            System.err.println("Failed to write to log: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing the log file: " + e.getMessage());
        }
    }

    // Ensuring resources are freed when object is collected
    @Override
    protected void finalize() throws Throwable {
        try {
            close();
            super.finalize();
        } catch (IOException e) {
            System.err.println("Error finalizing logger: " + e.getMessage());
        }
    }
}