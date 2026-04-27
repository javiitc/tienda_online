import Model.Cart;
import Model.Catalog;
import View.ShopApp;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Catalog catalog = new Catalog();
        catalog.productsFromFile("Java/Data/products.txt");

        ShopApp.cart        = new Cart();
        ShopApp.catalog     = catalog;
        ShopApp.invoicesDir = "Java/Data/Invoices";

        Application.launch(ShopApp.class, args);
    }
}