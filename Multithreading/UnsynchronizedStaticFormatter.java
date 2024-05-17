package Multithreading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UnsynchronizedStaticFormatter {

    public static void main(String[] args) {
        String filePath = "text1.java"; // Path to the Java file to be analyzed and modified
        String resolvedFilePath = resolveFormatterIssue(filePath);
        System.out.println("Resolved file written to: " + resolvedFilePath);
    }

    public static String resolveFormatterIssue(String filePath) {
        StringBuilder resolvedCode = new StringBuilder();
        boolean insideStaticBlock = false;
        boolean issueDetected = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;

            String currentVariableName = null;

            while ((line = reader.readLine()) != null) {
                // Detect start of static block
                if (line.contains("static ")) {
                    insideStaticBlock = true;
                    System.out.println("Start of static block detected at line: " + lineNumber);
                }
                
                // Detect variable instantiation inside static block
                if (insideStaticBlock && line.contains("=")) {
                    // Assuming that variable instantiation lines contain the "=" sign
                    String[] parts = line.split("=");
                    String variableDeclaration = parts[0].trim();
                    // Extract the variable name from the declaration
                    String[] variableParts = variableDeclaration.split("\\s+");
                    currentVariableName = variableParts[variableParts.length - 1];
                    System.out.println("Variable " + currentVariableName + " instantiation detected at line: " + lineNumber);
                }

                // Detect calls to the format() method on variables
                if (line.contains(".format()")) {
                    // Assuming that the format() method call follows the variable name
                    String[] parts = line.split("\\.");
                    String variableAndMethod = parts[0].trim(); // Get the part before the "."
                    // Extract the variable name
                    String[] variableParts = variableAndMethod.split("\\s+");
                    String variableNameFromMethodCall = variableParts[variableParts.length - 1];
                    System.out.println("Method call format() on variable " + variableNameFromMethodCall + " detected at line: " + lineNumber);
                    
                    // Compare the variable names
                    if (currentVariableName != null && currentVariableName.equals(variableNameFromMethodCall)) {
                        System.out.println("Variable names match.");
                        // Add the format() method call inside the synchronized block
                        resolvedCode.append("\t\t\t").append("synchronized (sdf) {").append("\n");
                        resolvedCode.append("\t\t\t\t").append(variableNameFromMethodCall).append(".format();").append("\n");
                        resolvedCode.append("\t\t\t").append("}").append("\n");
                    } else {
                        System.out.println("Variable names do not match.");
                    }
                } else {
                    // Append non-matching lines to resolvedCode
                    resolvedCode.append(line).append("\n");
                }

                lineNumber++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write resolved code to a new file
        String resolvedFilePath = filePath.replace(".java", "_resolved.java");
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(resolvedFilePath), resolvedCode.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resolvedFilePath;
    }
}
