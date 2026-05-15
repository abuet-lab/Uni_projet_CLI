package shop;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GraphQLService {

  private final String url;
  private final HttpClient httpClient;

  public GraphQLService(String url) {
    this.url = url;
    this.httpClient = HttpClient.newHttpClient();
  }

  public <T> T execute(GraphQLQuery<T> query) throws Exception {
    String requestBody = buildRequestBody(query.getQuery());
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new IOException("HTTP error: " + response.statusCode() + " - " + response.body());
    }
    return query.parseResponse(response.body());
  }

  private String buildRequestBody(String graphqlQuery) {
    String escaped = graphqlQuery.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    return "{\"query\": \"" + escaped + "\"}";
  }
}
