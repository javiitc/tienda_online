import Model.Catalog;
import Model.Cart;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Catalog catalog = new Catalog();
        catalog.productsFromFile("Java/Data/products.txt");
        boolean buying = true;
        Cart cart = new Cart();

        System.out.println("==================================");
        System.out.println("        MERCADONA DE TEMU");
        System.out.println("==================================");


        while (buying) {

            System.out.println("1. Agregar productos al carrito");
            System.out.println("2. Eliminar productos del carrito");
            System.out.println("3. Salir");

            int eleccion = sc.nextInt();

            switch (eleccion) {
                case 1:
                    catalog.showProducts();
                    System.out.println("\nSelecciona el producto que quieres agregar al carrito");

                    break;
                case 2:
                    break;
                case 3:
                    buying = false;
                    System.out.println("HASTA LA PRÓXIMA!");
                    break;
            }
        }
    }
}
