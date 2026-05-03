package main;

import java.util.List;

public class JsonFormatter implements OutputFormatter {

    @Override
    public void format(List<Product> data) {
        System.out.println("[");
        for (int i = 0; i < data.size(); i++) {
            Product p = data.get(i);
            System.out.print("  {\"name\": \"" + p.getName() +
                    "\", \"price\": " + p.getPrice() + "}");
            if (i < data.size() - 1) System.out.print(",");
            System.out.println();
        }
        System.out.println("]");
    }
}