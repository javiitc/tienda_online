package Model;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public Map<Integer, Integer> getQuantities() {
        return quantities;
    }

    public void checkout(String invoicesDir) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String txId  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        StringBuilder rows = new StringBuilder();
        for (int id : products.keySet()) {
            Product p = products.get(id);
            int qty = quantities.get(id);
            double lineTotal = p.getPrice() * qty;
            rows.append(String.format(
                "<tr><td>%s</td><td class=\"text-right\">%.2f&#x20AC;</td><td class=\"text-center\">%d</td><td class=\"text-right\">%.2f&#x20AC;</td></tr>%n",
                p.getName(), p.getPrice(), qty, lineTotal
            ));
        }

        String htmlContent;
        try {
            htmlContent = new String(Files.readAllBytes(Paths.get(invoicesDir + "/invoice.html")));
        } catch (IOException e) {
            System.out.println("Error: No se pudo leer la plantilla HTML.");
            return;
        }

        htmlContent = htmlContent
            .replace("{{FECHA}}", fecha)
            .replace("{{ID_TRANSACCION}}", txId)
            .replace("{{FILAS_PRODUCTOS}}", rows.toString())
            .replace("{{TOTAL_CARRITO}}", String.format("%.2f", getTotal()));

        String pdfPath = invoicesDir + "/recibo_" + txId + ".pdf";
        try (FileOutputStream os = new FileOutputStream(pdfPath)) {
            String baseUri = Paths.get(invoicesDir).toAbsolutePath().toUri().toString() + "/";
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, baseUri);
            builder.toStream(os);
            builder.run();
            System.out.println("Recibo generado: " + pdfPath);
            java.awt.Desktop.getDesktop().open(new java.io.File(pdfPath));
        } catch (Exception e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
        }

        products.clear();
        quantities.clear();
    }
}
