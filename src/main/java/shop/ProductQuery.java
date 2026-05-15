package shop;

import org.json.JSONObject;

/**
 * Requête GraphQL pour récupérer un produit spécifique. Correspond à : product(id: ID, slug:
 * String): Product
 */
public class ProductQuery implements GraphQLQuery<Product> {

  private final String id;
  private final String slug;

  public ProductQuery(String id) {
    this(id, null);
  }

  public ProductQuery(String id, String slug) {
    if (id == null && slug == null) {
      throw new IllegalArgumentException("id ou slug requis");
    }
    this.id = id;
    this.slug = slug;
  }

  @Override
  public String getQuery() {
    StringBuilder args = new StringBuilder();
    if (id != null) args.append("id: \\\"").append(id).append("\\\"");
    if (id != null && slug != null) args.append(", ");
    if (slug != null) args.append("slug: \\\"").append(slug).append("\\\"");

    return "query { "
        + "product("
        + args
        + ") { "
        + "id "
        + "name "
        + "slug "
        + "variants { "
        + "price "
        + "} "
        + "} "
        + "}";
  }

  @Override
  public Product parseResponse(String jsonResponse) throws Exception {
    JSONObject root = new JSONObject(jsonResponse);
    JSONObject data = root.getJSONObject("data");

    if (data.isNull("product")) {
      throw new Exception("Produit introuvable");
    }

    JSONObject product = data.getJSONObject("product");
    String name = product.getString("name");

    double price = 0.0;
    var variants = product.optJSONArray("variants");
    if (variants != null && variants.length() > 0) {
      price = variants.getJSONObject(0).getInt("price") / 100.0;
    }

    return new Product(name, price);
  }
}
