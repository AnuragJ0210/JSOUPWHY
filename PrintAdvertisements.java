import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrintAdvertisements {
  public static void main(String[] args) {
    // URL of the book reviews page
    String url = "https://www.goodreads.com/en/book/show/36204378#CommunityReviews";

    // CSV file to store scraped reviews
    String csvFile = "reviews.csv";

    // Scrape reviews from the URL and save them to the CSV file
    ReviewScraper.scrapeReviewsToCSV(url, csvFile);

    // Perform matching algorithm on the reviews
    MatchingAlgorithm.match();

    // Read authors from the CSV file
    csvFile = "reviews.csv";
    List<String> authors = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        // Use comma as separator to split the line
        String[] countryEntry = line.split(",");
        if (countryEntry.length > 0) { // Check if the line has at least one column
          String author = countryEntry[0].trim(); // Trim whitespace
          authors.add(author); // Add author to the list
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Read CS list and statistics list from files
    List<String> csList = readListFromFile("csList.txt");
    List<String> statisticsList = readListFromFile("statisticsList.txt");

    // Read CS advertisement and statistics advertisement from files
    String csAdvertisement = readStringFromFile("csAdvertisement.txt");
    String statisticsAdvertisement = readStringFromFile("statisticsAdvertisement.txt");

    // Print advertisements for each author based on their category
    for (String author : authors) {
      if (csList.contains(author)) { // If author is in CS list
        System.out.println(author + ", " + csAdvertisement);
        System.out.println();
      }
      if (statisticsList.contains(author)) { // If author is in statistics list
        System.out.println(author + ", " + statisticsAdvertisement);
        System.out.println();
      }
    }
  }

  // Method to read a list of strings from a file
  private static List<String> readListFromFile(String filename) {
    List<String> list = new ArrayList<>();
    try (Scanner scanner = new Scanner(Paths.get(filename))) {
      while (scanner.hasNextLine()) {
        list.add(scanner.nextLine().trim()); // Trim whitespace and add to the list
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  // Method to read a string from a file
  private static String readStringFromFile(String filename) {
    try {
      return new String(Files.readAllBytes(Paths.get(filename))).trim(); // Read file content and trim whitespace
    } catch (IOException e) {
      e.printStackTrace();
      return ""; // Return empty string in case of error
    }
  }
}
