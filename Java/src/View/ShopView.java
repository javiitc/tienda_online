package View;

import Model.Product;
import java.util.List;
import java.util.Scanner;

public class ShopView {
    // Scanner to read the user's keyboard input
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("\n=== TIENDA ===");
        System.out.println("1. Ver catálogo y añadir producto");
        System.out.println("2. Ver carrito");
        System.out.println("3. Eliminar producto del carrito");
        System.out.println("4. Pagar y generar recibo");
        System.out.println("5. Salir sin comprar");
        System.out.print("Elige una opción: ");
    }

    public String getUserInput() {
        return scanner.nextLine();
    }

    public void displayCatalog(List<Product> catalog) {
        System.out.println("\n--- PRODUCTOS ---");
        for (int i = 0; i < catalog.size(); i++) {
            Product p = catalog.get(i);
            System.out.println(p.toString());
        }
        System.out.println("-----------------------------");
    }

}
