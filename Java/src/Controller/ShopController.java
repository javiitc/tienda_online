package Controller;

import Model.Cart;
import Model.Product;
import Model.Catalog;
import View.ShopView;

public class ShopController {
    private Cart cart;
    private Catalog catalog;
    private ShopView view;

    public ShopController(Cart cart, Catalog catalog, ShopView view) {
        this.cart = cart;
        this.catalog = catalog;
        this.view = view;
    }

    public void start() {
        boolean running = true;
        while (running) {
            view.displayMenu();
            String choice = view.getUserInput();

            try {
                int option = Integer.parseInt(choice);

                switch (option) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        cart.showCart();
                        break;
                    case 3:
                        removeProduct();
                        break;
                    case 4:
                        checkout();
                        running = false;
                        break;
                    case 5:
                        System.out.println("Saliendo sin comprar. Hasta pronto!");
                        running = false; // Stops the loop
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, elige del 1 al 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido para el menú.");
            }
        }
    }

    private void addProduct() {
        view.displayCatalog(catalog.getAvailableProducts());
        System.out.println("Introduce el ID del producto que deseas añadir:");

        try {
            int id = Integer.parseInt(view.getUserInput());

            Product selectedProduct = catalog.getProductById(id);

            cart.addProduct(selectedProduct);

        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número.");
        }
    }

    private void removeProduct() {
        System.out.println("Introduce el ID del producto que deseas eliminar:");
        try {
            int id = Integer.parseInt(view.getUserInput());
            cart.removeProduct(id);
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número.");
        }
    }

    private void checkout() {
        if (cart.isEmpty()) {
            System.out.println("El carrito está vacío, no hay nada que pagar.");
        } else {
            cart.checkout("Java/Data/bills.txt");
        }
    }
}
