package BestPractices;

import java.io.*;
import java.util.logging.Logger;

// Need to import     private static final Logger logger = Logger.getLogger(textModified.class.getName());
// import java.util.logging.Logger;

public class SystemPrintln {
    public static void main(String[] args) {
        String inputFilePath = "text1.java";
        String outputDirectoryPath = "C:/RA/sample";

        SystemPrintln detector = new SystemPrintln();
        String outputFilePath = detector.detectSystemPrintln(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
    }

    
    public String detectSystemPrintln(String inputFilePath, String outputDirectoryPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            String className = extractClassName(inputFilePath);
            String newClassName = className + "Modified";
    
            String outputFileName = newClassName + ".java";
            String outputFilePath = outputDirectoryPath + "/" + outputFileName;
    
            boolean classFound = false;
    
            try (FileWriter fileWriter = new FileWriter(outputFilePath);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
        
                while ((line = br.readLine()) != null) {
                    // Check if the line is not a comment
                    if (!line.trim().startsWith("//")) {
                        // Replace System.print with logger
                        line = correctSystemPrintln(line);
                    }
    
                    // Replace the original class name with the new class name
                    if (!classFound && line.contains(className)) {
                        line = line.replace(className, newClassName);
                        classFound = true;
                    }
    
                    // Write each modified line to the output file
                    bufferedWriter.write(line);
                    bufferedWriter.newLine(); // Add a new line after each line
                }
        
                return outputFilePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
        
    
    private static String extractClassName(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("class")) {
                    // Extract class name from the line containing "class" keyword
                    int startIndex = line.indexOf("class") + "class".length();
                    int endIndex = line.indexOf("{");
                    return line.substring(startIndex, endIndex).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String correctSystemPrintln(String input) {
        // Perform replacements
        if (input.contains("System.out.println")) {
            // Replace System.out.println with logger.info
            input = input.replace("System.out.println", "logger.info");
        } else if (input.contains("System.out.print")) {
            // Replace System.out.print with logger.info
            input = input.replace("System.out.print", "logger.info");
        } else if (input.contains("System.err.println")) {
            // Replace System.err.println with logger.severe
            input = input.replace("System.err.println", "logger.severe");
        } else if (input.contains("System.err.print")) {
            // Replace System.err.print with logger.severe
            input = input.replace("System.err.print", "logger.severe");
        }
        return input;
    }

    private static boolean containsSystemPrintln(String line) {
        return line.contains("System.out.println") ||
               line.contains("System.out.print") ||
               line.contains("System.err.println") ||
               line.contains("System.err.print");
    }
}
