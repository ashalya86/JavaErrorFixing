package BestPractices;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class LogReplacer {

    private static final Logger logger = Logger.getLogger(LogReplacer.class.getName());

    public static void main(String[] args) {
        String filePath = "text.java"; // Provide the path to your Java file
        replaceLogStatements(filePath);
    }

    public static void replaceLogStatements(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("System.out.println")) {
                    // Replace System.out.print with logger.info
                    line = line.replace("System.out.println", "logger.info");
                } else if (line.contains("System.out.print")) {
                    // Replace System.out.println with logger.info
                    line = line.replace("System.out.print", "logger.info");
                } else if (line.contains("System.err.println")) {
                    // Replace System.err.print with logger.severe
                    line = line.replace("System.err.println", "logger.severe");
                } else if (line.contains("System.err.print")) {
                    // Replace System.err.println with logger.severe
                    line = line.replace("System.err.print", "logger.severe");
                }

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Error reading or writing file: " + e.getMessage());
            e.printStackTrace();
        }

        // Rename the temporary file to the original file
        try {
            // Delete the original file
            if (!new java.io.File(filePath).delete()) {
                logger.severe("Failed to delete the original file: " + filePath);
                return;
            }
            // Rename the temporary file to the original file
            if (!new java.io.File(filePath + ".tmp").renameTo(new java.io.File(filePath))) {
                logger.severe("Failed to rename the temporary file to the original file: " + filePath);
            }
        } catch (Exception e) {
            logger.severe("Error renaming file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
