import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicateRows {
    public static void main(String[] args) {
        String inputFile = "csList.txt"; // Change this to your input file name
        String outputFile = "csList.txt"; // Change this to your output file name

        removeDuplicateRows(inputFile, outputFile);
    }

    public static void removeDuplicateRows(String inputFile, String outputFile) {
        Set<String> uniqueRows = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // Check if the row is not already present
                if (!uniqueRows.contains(currentLine)) {
                    writer.write(currentLine);
                    writer.newLine();
                    // Add the row to the set to check for duplicates
                    uniqueRows.add(currentLine);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        }
    }
}