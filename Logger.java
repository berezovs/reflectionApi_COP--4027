import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private String fileName = null;
    private FileOutputStream out = null;
    private PrintWriter printer = null;

    Logger(String filename) {
        this.fileName = filename;
    }

    
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
