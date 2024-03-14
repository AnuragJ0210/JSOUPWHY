import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MatchingAlgorithm {
    // Method to perform matching algorithm
    public static void match() {
        // Read reviews from CSV file
        String csvFile = "reviews.csv";
        List<List<String>> wordLists = new ArrayList<>(); // List to store word lists from reviews
        List<String> authors = new ArrayList<>(); // List to store authors of reviews

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split each line by comma
                String[] countryEntry = line.split(",");
                if (countryEntry.length > 1) { // Check if line has at least two columns
                    String wordsInSecondColumn = countryEntry[1];
                    String author = countryEntry[0];
                    // Split second column into words and add to wordLists
                    List<String> wordsList = Arrays.asList(wordsInSecondColumn.split("\\s+"));
                    wordLists.add(wordsList);
                    authors.add(author); // Add author to authors list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read targeted words from file
        csvFile = "targetedwords.txt";
        String cvsSplitBy = ",";
        ArrayList<ArrayList<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> row = new ArrayList<>();
                String[] values = line.split(cvsSplitBy);
                for (String value : values) {
                    row.add(value);
                }
                records.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert 2D ArrayList to 2D array
        String[][] array2D = new String[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            ArrayList<String> row = records.get(i);
            array2D[i] = row.toArray(new String[0]);
        }

        // Lists to store authors with high ratings in different categories
        List<String> csList = new ArrayList<>();
        List<String> statisticsList = new ArrayList<>();

        // Perform matching and calculate ratings
        double authorCSRating = 0;
        double authorStatisticsRating = 0;
        for (int i = 0; i < wordLists.size(); i++) {
            authorCSRating = 0;
            authorStatisticsRating = 0;
            for (int j = 0; j < wordLists.get(i).size(); j++) {
                for (int k = 0; k < array2D.length; k++) {
                    // If word matches, update ratings
                    if (wordLists.get(i).get(j).equals(array2D[k][0])) {
                        authorCSRating += Double.parseDouble(array2D[k][1]);
                        authorStatisticsRating += Double.parseDouble(array2D[k][2]);
                    }
                }
            }
            // Output author ratings
            System.out.println(authors.get(i) + " CS Rating: " + authorCSRating);
            System.out.println(authors.get(i) + " Statistics Rating: " + authorStatisticsRating);
            System.out.println();

            // Add authors to respective lists based on ratings
            if ((authorCSRating > 10) && (authorCSRating > authorStatisticsRating)) {
                csList.add(authors.get(i));
            }

            if ((authorStatisticsRating > 10) && (authorStatisticsRating > authorCSRating)) {
                statisticsList.add(authors.get(i));
            }
        }

        // Write lists to files
        try {
            Files.write(Paths.get("csList.txt"), csList);
            Files.write(Paths.get("statisticsList.txt"), statisticsList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}