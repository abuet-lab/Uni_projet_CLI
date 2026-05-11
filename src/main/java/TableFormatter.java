package java;

import java.util.List;

public class TableFormatter implements OutputFormatter {

    @Override
    public void format(List<Product> data) {
        System.out.printf("%-20s %s%n", "Nom", "Prix");
        System.out.println("-".repeat(30));
        for (Product p : data) {
            System.out.printf("%-20s %.2f CHF%n", p.getName(), p.getPrice());
        }
    }
}
