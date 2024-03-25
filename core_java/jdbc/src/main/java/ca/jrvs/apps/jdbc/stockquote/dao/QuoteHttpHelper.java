package ca.jrvs.apps.jdbc.stockquote.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

public class QuoteHttpHelper {
    private String apiKey;
    private OkHttpClient client;

    QuoteHttpHelper(String apiKey, OkHttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        Quote quote = null;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json"))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());
            JsonNode quoteNode = rootNode.path("Global Quote");
            Timestamp currTime = new Timestamp(System.currentTimeMillis());
            quote = mapper.treeToValue(quoteNode, Quote.class);
            quote.setTimestamp(currTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (quote == null) {
            throw new IllegalArgumentException("No data was found for the given symbol: " + symbol);
        }

        return quote;
    }

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        QuoteHttpHelper helper = new QuoteHttpHelper("e5236d6e9amsh66ed46c2553862cp199731jsnd34fe7241ab8", client);
        Quote quote = helper.fetchQuoteInfo("MSFT");
        System.out.println(quote);
    }

}
