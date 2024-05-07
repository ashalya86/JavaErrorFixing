package Performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class StringInstantiation {
    public static void main(String[] args) {
        String inputFilePath = "ReadFiles/UseIndexOfCharExample.java";
        String outputDirectoryPath = "ReadFiles";
      
        StringInstantiation detector = new StringInstantiation();
        String outputFilePath = detector.correctAppendCharacterWithChar(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
    }
    public String correctAppendCharacterWithChar(String inputFilePath, String outputDirectoryPath) {
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
                        line = stringInstantiationCorrector(line);
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

    public static String stringInstantiationCorrector(String line) {
            Pattern pattern = Pattern.compile("private\\s+String\\s+(\\w+)\\s+=\\s+new\\s+String\\(\"(.*?)\"\\);");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String variableName = matcher.group(1);
                    String value = matcher.group(2);
                    if (value.length() == 1 ) {
                        // Value is a single character enclosed within single quotes
                        line = "String " + variableName + " = '" + value + "';";
                    } else {
                        // Value is enclosed within double quotes
                        line = "String " + variableName + " = \"" + value + "\";";
                    }
                } 
                return line;
    }    
}
