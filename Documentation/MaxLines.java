package Documentation;

import java.io.*;

// To coveer this case: consider 6 lines;

public class MaxLines {
    public static void main(String[] args) {
        String inputFilePath = "text1.java";
        String outputDirectoryPath = "C:/RA/sample";

        MaxLines detector = new MaxLines();
        String outputFilePath = detector.detectMaxLines(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
    }

    
    public String detectMaxLines(String inputFilePath, String outputDirectoryPath) {
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
                        // Correct the comment length
                        line = correctMaxLines(line);
    
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

    public String correctMaxLines(String input) {
        System.out.println("input " + input);
        // Detect whether the input is a comment
        input = input.trim();
        if (input.startsWith("//") || input.startsWith("/*")) {
            System.out.println("detected");
            // If it is a comment, then measure the length
            String[] lines = input.split("\n");
            StringBuilder correctedInput = new StringBuilder();
            for (String line : lines) {
                if (line.length() > 80) {
                    // If line exceeds 80 characters, put line breaks
                    int newLines = (int) Math.ceil((double) line.length() / 80);
                    for (int i = 0; i < newLines; i++) {
                        correctedInput.append(line.substring(i * 80, Math.min((i + 1) * 80, line.length()))).append("\n");
                    }
                } else {
                    correctedInput.append(line).append("\n");
                }
            }
            return correctedInput.toString();
        } else {
            return input;
        }
    }
      
}
