package Performance;

import java.io.*;
import java.util.regex.*;


public class ConsecutiveLiteralAppends {

    public static void main(String[] args) {
        String inputFilePath = "ReadFiles/ConsecutiveLiteralAppendsExample.java";
        String outputDirectoryPath = "ReadFiles";
      
        ConsecutiveLiteralAppends detector = new ConsecutiveLiteralAppends();
        String outputFilePath = detector.detectConsecutiveLiteralAppends(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
    }

    public String detectConsecutiveLiteralAppends(String inputFilePath, String outputDirectoryPath) {
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
                        line = correctConsecutiveLiteralAppends(line);
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

    public static String correctConsecutiveLiteralAppends(String input) {
        // Regular expression pattern to match the variable name before ".append"
        Pattern pattern = Pattern.compile("(\\w+)\\.append");
        Matcher matcher = pattern.matcher(input);

        String variableName = "";
        if (matcher.find()) {
            variableName = matcher.group(1); // Extract the variable name before ".append"
        

        // Regular expression pattern to match strings within double or single quotes, or numeric values
        pattern = Pattern.compile("\"[^\"]*\"|'[^']*'|\\d+");
        matcher = pattern.matcher(input);

        StringBuilder replacement = new StringBuilder();
        // Iterate over matches and extract the strings
        while (matcher.find()) {
            String literal = matcher.group(); // Extract the matched literal
            // Remove the quotes if they exist
            literal = literal.replace("\"", "").replace("'", "");
            replacement.append(literal);
        }
        return variableName + ".append(\"" + replacement.toString() + "\");";
    }else{
        return input;
    }
    }
}
