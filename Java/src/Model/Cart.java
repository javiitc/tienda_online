package Model;

import Model.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private Map<Integer, Product> products = new LinkedHashMap<>();
    private Map<Integer, Integer> quantities = new LinkedHashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            System.out.println("Producto no encontrado");
            return;
        }
        int id = product.getId();
        if (products.containsKey(id)) {
            quantities.put(id, quantities.get(id) + 1);
        } else {
            products.put(id, product);
            quantities.put(id, 1);
        }
        System.out.println("Añadido: " + product.getName());
    }

    public boolean removeProduct(int id) {
        if (!products.containsKey(id)) {
            System.out.println("Producto no encontrado en el carrito.");
            return false;
        }
        int qty = quantities.get(id);
        if (qty > 1) {
            quantities.put(id, qty - 1);
        } else {
            products.remove(id);
            quantities.remove(id);
        }
        System.out.println("Eliminado: producto con ID " + id);
        return true;
    }

    public double getTotal() {
        double total = 0;
        for (int id : products.keySet()) {
            total += products.get(id).getPrice() * quantities.get(id);
        }
        return total;
    }

    public void showCart() {
        if (products.isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }
        System.out.println("--- Carrito ---");
        for (int id : products.keySet()) {
            Product p = products.get(id);
            int qty = quantities.get(id);
            System.out.printf("  %s x%d — %.2f€%n", p.getName(), qty, p.getPrice() * qty);
        }
        System.out.printf("  TOTAL: %.2f€%n", getTotal());
        System.out.println("---------------");
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void checkout(String billsFilePath) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        StringBuilder recibo = new StringBuilder();
        recibo.append("=== RECIBO ===\n");
        recibo.append("Fecha: ").append(fecha).append("\n");
        for (int id : products.keySet()) {
            Product p = products.get(id);
            int qty = quantities.get(id);
            recibo.append(String.format("%s x%d — %.2f€%n", p.getName(), qty, p.getPrice() * qty));
        }
        recibo.append(String.format("TOTAL: %.2f€%n", getTotal()));
        recibo.append("==============\n");

        try (FileWriter fw = new FileWriter(billsFilePath, true)) {
            fw.write(recibo.toString());
            System.out.println("Compra realizada. Recibo guardado en " + billsFilePath);
        } catch (IOException e) {
            System.out.println("Error al guardar el recibo: " + e.getMessage());
        }

        products.clear();
        quantities.clear();
    }
}