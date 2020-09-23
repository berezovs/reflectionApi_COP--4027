/*
Author: Serghei Berezovschi
Project: Reflection(Project1)
Class: COP--4027
*/
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Logger {
    private String fileName = null;
    private FileOutputStream out = null;
    private PrintWriter printer = null;

    Logger(String filename) {
        this.fileName = filename;
    }

    //creates a log file if it doesn't already exist and appends log statement to the end of file
    public void log(String logMessage) {
        try {
            this.out = new FileOutputStream(fileName, true);
            this.printer = new PrintWriter(out);

            printer.format("%s\n", logMessage);

            printer.close();
            out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
