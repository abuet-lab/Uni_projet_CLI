package shop;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Requête GraphQL pour récupérer la liste des produits.
 * Correspond à : products(options: ProductListOptions): ProductList!
 */
public class ProductsQuery implements GraphQLQuery<List<Product>> {

    private final int take;
    private final int skip;

    public ProductsQuery() {
        this(10, 0);
    }

    public ProductsQuery(int take, int skip) {
        this.take = take;
        this.skip = skip;
    }

    @Override
    public String getQuery() {
        return "query { "
                + "products(options: { take: " + take + ", skip: " + skip + " }) { "
                + "items { "
                + "id "
                + "name "
                + "slug "
                + "variants { "
                + "price "
                + "} "
                + "} "
                + "totalItems "
                + "} "
                + "}";
    }

    @Override
    public List<Product> parseResponse(String jsonResponse) throws Exception {
        JSONObject root = new JSONObject(jsonResponse);
        JSONObject data = root.getJSONObject("data");
        JSONObject products = data.getJSONObject("products");
        JSONArray items = products.getJSONArray("items");

        List<Product> result = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String name = item.getString("name");

            double price = 0.0;
            JSONArray variants = item.optJSONArray("variants");
            if (variants != null && variants.length() > 0) {
                price = variants.getJSONObject(0).getInt("price") / 100.0;
            }

            result.add(new Product(name, price));
        }
        return result;
    }
}