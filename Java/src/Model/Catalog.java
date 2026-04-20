package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Catalog {
    private List<model.Product> availableProducts = new ArrayList<>();

    public void productsFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (int i = 0; i < lines.size(); i++) {

                String line = lines.get(i);

                String[] parts = line.split(";");

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);

                availableProducts.add(new model.Product(id, name, price));
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read the file at " + filePath);
        }

    }

    public List<model.Product> getAvailableProducts() {
        return availableProducts;
    }

    public model.Product getProductById(int id) {
        for (model.Product p : availableProducts) {
            if (p.getId() == id) {
                return new model.Product(p.getId(), p.getName(), p.getPrice());
            }
        }
        return null;
    }
}
