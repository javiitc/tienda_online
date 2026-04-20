import Controller.ShopController;
import Model.Cart;
import Model.Catalog;
import View.ShopView;

public class Main {
    public static void main(String[] args) {
        Catalog catalog = new Catalog();
        catalog.productsFromFile("Java/Data/products.txt");

        Cart cart = new Cart();

        ShopView view = new ShopView();

        ShopController controller = new ShopController(cart, catalog, view);

        controller.start();
    }
}