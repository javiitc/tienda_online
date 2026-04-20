package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Catalog {

    private List<Product> availableProducts = new ArrayList<>();

    public void productsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                availableProducts.add(new Product(id, name, price));
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read the file at " + filePath);
        }
    }

    public List<Product> getAvailableProducts() {
        return availableProducts;
    }

    public void showProducts() {
        System.out.println("--- Catálogo ---");
        for (Product p : availableProducts) {
            System.out.println("  " + p);
        }
        System.out.println("----------------");
    }

    public Product getProductById(int id) {
        for (Product p : availableProducts) {
            if (p.getId() == id) {
                return new Product(p.getId(), p.getName(), p.getPrice());
            }
        }
        return null;
    }
}