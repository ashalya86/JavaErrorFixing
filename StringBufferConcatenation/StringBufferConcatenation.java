import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StringBufferConcatenation {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <inputFilePath> <outputDirectoryPath>");
            return;
        }
        
        String inputFilePath = args[0];
        String outputDirectoryPath = args[1];
        
        StringBufferConcatenation sm = new StringBufferConcatenation();
        String outputFilePath = sm.StringBCCorrector(inputFilePath, outputDirectoryPath);
        sm.fixStringBuffer(outputFilePath);   
        
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

    public String StringBCCorrector(String inputFilePath, String outputDirectoryPath) {
        String outputFilePath = null; // Initialize outputFilePath
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            int lineNumber = 0;
    
            String className = extractClassName(inputFilePath);
            String newClassName = className + "Modified"; // Specify the new class name
    
            if (className != null) {
                System.out.println("Class Name: " + className);
                System.out.println("New Class Name: " + newClassName);
            } else {
                System.out.println("No class name found.");
            }
    
            String outputFileName = newClassName + ".java"; // Construct the output file name
            outputFilePath = outputDirectoryPath + "/" + outputFileName; // Construct the full output file path
    
            try (FileWriter fileWriter = new FileWriter(outputFilePath);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
    
                while ((line = br.readLine()) != null) {
                    lineNumber++;
    
                    // Check if the line is not a comment
                    if (!line.trim().startsWith("//")) {
                        if (line.contains("+=")) {
                            System.out.println("  Found '+=' operator at line " + lineNumber);
    
                            String[] parts = line.split("\\+=");
                            if (parts.length > 1) {
                                String variableName = parts[0].trim();
                                System.out.println("Variable name: " + variableName);
                                String rest = parts[1].trim().substring(0, parts[1].trim().length() - 1);
    
                                if (rest.startsWith("\"")) {
                                    System.out.println(
                                            "String buffer concatenation fault detected at line " + lineNumber
                                                    + ": " + line.trim());
                                    line = "StringBuffer " + variableName + "= new StringBuffer();";
                                    bufferedWriter.write(line);
                                    bufferedWriter.newLine();
                                    line = variableName +".append(" + rest + ");";
                                    System.out.println(line);
    
                                } else {
                                    System.out.println(
                                            "Consider using StringBuilder at line " + lineNumber
                                                    + ": " + line.trim());
                                }
                            }
                        }
                    }
    
                    // Replace the original class name with the new class name
                    line = line.replace(className, newClassName);
                    // Write each modified line to the output file
                    bufferedWriter.write(line);
                    bufferedWriter.newLine(); // Add a new line after each line
                }
    
                System.out.println("File successfully written to: " + outputFilePath);
    
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Return the outputFilePath
        return outputFilePath;
    }
    
 // unnesseary initialisation will be deleted:   
    public  void fixStringBuffer(String fileName) {
        List<String> lines = new ArrayList<>();
        String line;
    
        try {
            File file = new File(fileName);
            System.out.println("file... "+ file);
            System.out.println("File path: " + file.getAbsolutePath());
    
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            // Read each line of the file until the end
            while ((line = br.readLine()) != null) {
                // Use a regular expression to detect incorrect initialization
                if (line.matches("\\s*String\\s+\\w+\\s*=\\s*\".*\";")) {
                    // Replace incorrect initialization with nothing to delete it
                    line = "";
                }
                lines.add(line);
            }
            
            fr.close();
            br.close();
    
            FileWriter fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
                out.write(s + "\n");
            out.flush();
            out.close();
            System.out.println("Error corrected. Updated file: " + fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        return null; // Return null if class name is not found
    }
}

    
