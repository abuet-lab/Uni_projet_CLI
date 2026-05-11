package shop;

import java.util.List;
import picocli.CommandLine;

@picocli.CommandLine.Command(name = "list", description = "Affiche la liste des produits")
public class ListCommand extends Command implements Runnable {

  @CommandLine.Option(names = "--format", defaultValue = "table")
  private final String format;
  @CommandLine.ParentCommand
  private CLI parent;

  public ListCommand(String url, String format) {
    super(url);
    this.format = format;
    if (format != null && !format.equals("table") && !format.equals("json")) {
      throw new IllegalArgumentException("Invalid format");
    }
  }

  public String getUrl() {
    return this.url;
  }

  public String getFormat() {
    return this.format;
  }

  @Override
  public void run() {
    this.url = parent.getUrl();
    if (this.url == null) this.url = System.getenv("URL");
    try {
      execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute() throws Exception {
    List<Product> products = fetchProducts();
    OutputFormatter formatter;
    if (format.equals("json")) {
      formatter = new JsonFormatter();
    } else {
      formatter = new TableFormatter();
    }
    formatter.format(products);
  }

  private List<Product> fetchProducts() {
    return List.of(
            new Product("T-shirt", 19.99),
            new Product("Pantalon", 49.99),
            new Product("Chaussures", 89.99));
  }
}