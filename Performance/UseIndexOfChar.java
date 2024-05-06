package Performance;

import java.io.*;
import java.util.regex.*;


public class UseIndexOfChar {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <inputFilePath> <outputDirectoryPath>");
            return;
        }
        
        String inputFilePath = args[0];
        String outputDirectoryPath = args[1];
        
        UseIndexOfChar detector = new UseIndexOfChar();
        String outputFilePath = detector.correctAppendCharacterWithChar(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
      
        // Run the Java file from inputFilePath and capture its output
        try {
            Process process = Runtime.getRuntime().exec("java -cp " + outputDirectoryPath + " " + outputFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("Output of the Java file:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error running the Java file: " + e.getMessage());
        }
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
                        line = useIndexOfCharCorrector(line);
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

    public static String useIndexOfCharCorrector(String input) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(input);
        
        // Find and print all strings inside double quotes
        while (matcher.find()) {
            String match = matcher.group(1); // Group 1 contains the string inside the quotes
            // System.out.println("String inside double quotes: " + match);
            if (match.length() == 1) {
                // The matched string is a single element
                input = input.replaceAll("\"" + match + "\"", "'" + match + "'");
                System.out.println("Single element: " + match);
                System.out.println("Input: " + input);

            }
        }
        return input;
    }    
}
