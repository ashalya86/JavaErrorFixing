package Performance;

import java.io.*;
import java.util.regex.*;


public class UseStringBufferLength {
    public static void main(String[] args) {
        String inputFilePath = "ReadFiles/UseStringBufferLengthExample.java";
        String outputDirectoryPath = "ReadFiles";
      
        UseStringBufferLength detector = new UseStringBufferLength();
        String outputFilePath = detector.detectPerformanceViolation(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
    }


    public String detectPerformanceViolation(String inputFilePath, String outputDirectoryPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            String className = extractClassName(inputFilePath);
            String newClassName = className + "Modified";

            String outputFileName = newClassName + ".java";
            String outputFilePath = outputDirectoryPath + "/" + outputFileName;

            try (FileWriter fileWriter = new FileWriter(outputFilePath);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
    
                while ((line = br.readLine()) != null) {
                    // Check if the line is not a comment
                    if (!line.trim().startsWith("//")) {
                        // Correct empty string concatenations
                        line = correctPerformanceViolation(line);
                    }
    
                    // Replace the original class name with the new class name
                    line = line.replace(className, newClassName);
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

    public static String correctPerformanceViolation(String input) { 
         // Pattern to match the violation: sb.toString().equals("")
         Pattern pattern = Pattern.compile("\\.toString\\(\\)\\.equals\\(\"\"\\)");
         Matcher matcher = pattern.matcher(input);
         if (matcher.find()) {
            input = input.replaceAll("\\.toString\\(\\)\\.equals\\(\"\"\\)", ".length() == 0");
            System.out.println("Violation detected in file "+ input);
        }
        return input;
    }
    
}
