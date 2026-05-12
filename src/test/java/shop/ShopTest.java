package shop;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ShopTest {

  // Tests ListCommand
  @org.junit.jupiter.api.Test
  public void testListCommandCreation() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "table");
    assertNotNull(cmd);
  }

  @org.junit.jupiter.api.Test
  public void testListCommandUrlStored() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "table");
    assertEquals("http://localhost:3000/shop-api", cmd.getUrl());
  }

  @org.junit.jupiter.api.Test
  public void testListCommandFormatTable() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "table");
    assertEquals("table", cmd.getFormat());
  }

  @org.junit.jupiter.api.Test
  public void testListCommandFormatJson() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "json");
    assertEquals("json", cmd.getFormat());
  }

  @org.junit.jupiter.api.Test
  public void testListCommandInvalidFormat() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new ListCommand("http://localhost:3000/shop-api", "xml"));
  }

  // Tests URL

  @org.junit.jupiter.api.Test
  public void testListCommandNullUrl() {
    assertThrows(IllegalArgumentException.class, () -> new ListCommand(null, "table"));
  }

  @org.junit.jupiter.api.Test
  public void testListCommandEmptyUrl() {
    assertThrows(IllegalArgumentException.class, () -> new ListCommand("", "table"));
  }

  // Tests CLI principal

  @org.junit.jupiter.api.Test
  public void testCLINoArgs() {
    assertThrows(IllegalArgumentException.class, () -> CLI.main(new String[] {}));
  }

  @org.junit.jupiter.api.Test
  public void testCLIWithUrlBeforeCommand() {
    CLI cli = new CLI();
    int exitCode =
        new picocli.CommandLine(cli).execute("--url", "http://localhost:3000/shop-api", "list");
    assertEquals(0, exitCode);
    assertNotNull(cli);
  }

  @org.junit.jupiter.api.Test
  public void testCLIWithUrlAfterCommand() {
    CLI cli = new CLI();
    int exitCode =
        new picocli.CommandLine(cli).execute("list", "--url", "http://localhost:3000/shop-api");
    assertEquals(0, exitCode);
    assertNotNull(cli);
  }

  // la requête produit le bon query GraphQL

  @org.junit.jupiter.api.Test
  public void testProductsQueryContainsProducts() {
    ProductsQuery query = new ProductsQuery();
    assertTrue(query.getQuery().contains("products"));
  }

  @org.junit.jupiter.api.Test
  public void testProductsQueryContainsItems() {
    ProductsQuery query = new ProductsQuery();
    assertTrue(query.getQuery().contains("items"));
  }

  @org.junit.jupiter.api.Test
  public void testProductsQueryContainsName() {
    ProductsQuery query = new ProductsQuery();
    assertTrue(query.getQuery().contains("name"));
  }

  @org.junit.jupiter.api.Test
  public void testProductQueryContainsId() {
    ProductQuery query = new ProductQuery("42");
    assertTrue(query.getQuery().contains("42"));
  }

  @org.junit.jupiter.api.Test
  public void testProductQueryContainsSlug() {
    ProductQuery query = new ProductQuery(null, "laptop");
    assertTrue(query.getQuery().contains("laptop"));
  }

  @org.junit.jupiter.api.Test
  public void testProductQueryRequiresIdOrSlug() {
    assertThrows(IllegalArgumentException.class, () -> new ProductQuery(null, null));
  }

  // le JSON est bien converti en objets Java

  @org.junit.jupiter.api.Test
  public void testProductsQueryParseResponse() throws Exception {
    ProductsQuery query = new ProductsQuery();
    String fakeJson =
        "{"
            + "\"data\": {"
            + "  \"products\": {"
            + "    \"items\": ["
            + "      { \"id\": \"1\", \"name\": \"Laptop\", \"slug\": \"laptop\", \"variants\": [{ \"price\": 129900 }] },"
            + "      { \"id\": \"2\", \"name\": \"Tablet\", \"slug\": \"tablet\", \"variants\": [{ \"price\": 32900 }] }"
            + "    ],"
            + "    \"totalItems\": 2"
            + "  }"
            + "}}";

    List<Product> products = query.parseResponse(fakeJson);

    assertEquals(2, products.size());
    assertEquals("Laptop", products.get(0).name());
    assertEquals(1299.0, products.get(0).price(), 0.01);
    assertEquals("Tablet", products.get(1).name());
    assertEquals(329.0, products.get(1).price(), 0.01);
  }

  @org.junit.jupiter.api.Test
  public void testProductsQueryParseEmptyList() throws Exception {
    ProductsQuery query = new ProductsQuery();
    String fakeJson =
        "{"
            + "\"data\": {"
            + "  \"products\": {"
            + "    \"items\": [],"
            + "    \"totalItems\": 0"
            + "  }"
            + "}}";

    List<Product> products = query.parseResponse(fakeJson);
    assertEquals(0, products.size());
  }

  @org.junit.jupiter.api.Test
  public void testProductQueryParseResponse() throws Exception {
    ProductQuery query = new ProductQuery("1");
    String fakeJson =
        "{"
            + "\"data\": {"
            + "  \"product\": {"
            + "    \"id\": \"1\","
            + "    \"name\": \"Laptop\","
            + "    \"slug\": \"laptop\","
            + "    \"variants\": [{ \"price\": 129900 }]"
            + "  }"
            + "}}";

    Product product = query.parseResponse(fakeJson);
    assertEquals("Laptop", product.name());
    assertEquals(1299.0, product.price(), 0.01);
  }

  @org.junit.jupiter.api.Test
  public void testProductQueryParseNotFound() {
    ProductQuery query = new ProductQuery("999");
    String fakeJson = "{\"data\": {\"product\": null}}";
    assertThrows(Exception.class, () -> query.parseResponse(fakeJson));
  }
}
