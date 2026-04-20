import Model.Catalog;

public class Main {
    public static void main(String[] args) {
        Catalog catalog = new Catalog();


        catalog.productsFromFile("Data/products.txt");
    }
}
