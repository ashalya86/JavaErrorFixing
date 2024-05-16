package Documentation;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UncommentedEmptyMethodBody {
    public static void main(String[] args) {
        String inputFilePath = "text1.java";
        String outputDirectoryPath = "C:/RA/sample";

        UncommentedEmptyMethodBody detector = new UncommentedEmptyMethodBody();
        String outputFilePath = detector.detectUncommentedEmptyMethodBody(inputFilePath, outputDirectoryPath);
        System.out.println("Corrected file written to: " + outputFilePath);
    }

    public String detectUncommentedEmptyMethodBody(String inputFilePath, String outputDirectoryPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            String className = extractClassName(inputFilePath);
            String newClassName = className + "Modified";

            String outputFileName = newClassName + ".java";
            String outputFilePath = outputDirectoryPath + "/" + outputFileName;

            boolean classFound = false;
            boolean insideMethod = false; // Track if currently inside a method

            try (FileWriter fileWriter = new FileWriter(outputFilePath);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                StringBuilder code = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    code.append(line).append("\n");

                    // Replace the original class name with the new class name
                    if (!classFound && line.contains(className)) {
                        line = line.replace(className, newClassName);
                        classFound = true;
                    }

                    // Detect start of method
                    if (line.contains("{")) {
                        insideMethod = true;
                    }

                    // Detect end of method and check if it's empty
                    if (insideMethod && line.contains("}") && correctUncommentedEmptyMethodBody(code.toString())) {
                        // Insert default comment inside the empty method body
                        bufferedWriter.write("    // TODO: Implement method functionality\n");
                    }

                    // Write each modified line to the output file
                    bufferedWriter.write(line);
                    bufferedWriter.newLine(); // Add a new line after each line

                    // Reset insideMethod flag if reached end of method
                    if (line.contains("}")) {
                        insideMethod = false;
                    }
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

    private boolean correctUncommentedEmptyMethodBody(String code) {
        // Regular expression to match empty method bodies without comments
        String regex = "public\\s+void\\s+\\w+\\(\\)\\s*\\{\\s*\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);

        // Iterate through matches and check if there are comments inside the method
        while (matcher.find()) {
            int startPos = matcher.start();
            int endPos = matcher.end();
            String methodCode = code.substring(startPos, endPos);

            // Check if there are any comments inside the method
            if (!methodCode.contains("//") && !methodCode.contains("/*")) {
                // Found an uncommented empty method body
                System.out.println("Uncommented empty method body detected:");
                System.out.println(methodCode);
                return true;
            }
        }
        return false;
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
}
