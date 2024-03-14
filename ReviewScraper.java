import org.jsoup.Jsoup; // Import Jsoup for web scraping
import org.jsoup.nodes.Document; // Import Jsoup classes for document manipulation
import org.jsoup.nodes.Element; // Import Jsoup classes for element manipulation
import org.jsoup.select.Elements; // Import Jsoup classes for element selection
import java.io.FileWriter; // Import FileWriter to write to CSV files
import java.io.IOException; // Import IOException for error handling
import java.util.Arrays; // Import Arrays for array manipulation
import java.util.List; // Import List for list manipulation

public class ReviewScraper {
    // Method to scrape reviews from a URL and save them to a CSV file
    public static void scrapeReviewsToCSV(String url, String csvFile) {
        // Define CSV file headers
        List<String> headers = Arrays.asList("Reviewer Name", "Review Text", "Rating", "Date", "Metadata");

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write headers to CSV file
            writer.append(String.join(",", headers)); // Join headers with commas
            writer.append("\n"); // Move to next line after writing headers

            // Connect to the URL and get the HTML document
            Document document = Jsoup.connect(url).get();

            // Select all review elements
            Elements reviews = document.select(".ReviewsList .ReviewCard");

            // Iterate over each review element
            for (Element review : reviews) {
                // Extract reviewer name from the review element
                String reviewerName = review.select(".ReviewerProfile__name").text();

                // Extract review text from the review element and remove commas
                String reviewText = review.select(".ReviewText__content").text().replaceAll(",", "");

                // Extract rating from the review element
                String rating = review.select(".RatingStars").attr("aria-label");
                rating = rating.length() >= 9 ? rating.substring(7, 8) : ""; // Extract rating value

                // Extract date text from the review element
                String dateText = review.select(".ReviewCard__row a").text();

                // Extract metadata from the review element
                String metadata = review.select(".ReviewerProfile__meta span").first().text();

                // Manually add quotes around reviewText and escape existing quotes
                reviewText = "\"" + reviewText.replace("\"", "\\\"") + "\"";

                // Write review data to CSV file
                List<String> data = Arrays.asList(reviewerName, reviewText, rating, dateText, metadata);
                writer.append(String.join(",", data)); // Join review data with commas
                writer.append("\n"); // Move to next line for the next review
            }
        } catch (IOException e) { // Handle IOException
            e.printStackTrace(); // Print stack trace if an exception occurs
        }
    }
}