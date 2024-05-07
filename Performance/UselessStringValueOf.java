package Performance;

import java.io.*;
import java.util.regex.*;


public class UselessStringValueOf {

    public static void main(String[] args) {
        String inputFilePath = "ReadFiles/UselessStringValueOfExample.java";
        String outputDirectoryPath = "ReadFiles";
      
        UselessStringValueOf detector = new UselessStringValueOf();
        String outputFilePath = detector.detectUselessStringValueOf(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
    }

    public String detectUselessStringValueOf(String inputFilePath, String outputDirectoryPath) {
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
                        line = correctUselessStringValueOf(line);
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

    public static String correctUselessStringValueOf(String input) {
            // Regular expression patterns for detecting the violations
            Pattern pattern1 = Pattern.compile("\\+\\s*String\\.valueOf\\((.*?)\\)"); // Case 1
            Pattern pattern2 = Pattern.compile("String\\.valueOf\\((.*?)\\)\\s*\\+"); // Case 2
            Pattern pattern3 = Pattern.compile("append\\(\\s*String\\.valueOf\\((.*?)\\)\\s*\\)"); // Case 3

            // Check for violation cases in the line
            Matcher matcher1 = pattern1.matcher(input);
            Matcher matcher2 = pattern2.matcher(input);
            Matcher matcher3 = pattern3.matcher(input);

            // Case 1, 2, 3: Detecting concatenating strings with String.valueOf()
            if (matcher1.find() || matcher2.find() ||  matcher3.find()){
                input = input.replaceAll("String\\.valueOf\\(([^)]+)\\)", "$1");
                System.out.println("Violation detected at line "  + ": " + input);
            }

        return input;
    }
        
    
}
