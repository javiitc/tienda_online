package View;

import Model.Cart;
import Model.Catalog;
import Model.Product;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class ShopApp extends Application {

    public static Cart    cart;
    public static Catalog catalog;
    public static String  invoicesDir;

    private StackPane root;

    private static final String BG      = "#f8f9fa";
    private static final String ACCENT  = "#3498db";
    private static final String SUCCESS = "#2ecc71";
    private static final String DANGER  = "#e74c3c";
    private static final String WHITE   = "#ffffff";

    private static final Map<String, String> EMOJIS = new HashMap<>();
    static {
        EMOJIS.put("coca cola", "🥤");
        EMOJIS.put("agua",      "💧");
        EMOJIS.put("pan",       "🍞");
        EMOJIS.put("leche",     "🥛");
        EMOJIS.put("yogur",     "🍶");
        EMOJIS.put("queso",     "🧀");
        EMOJIS.put("jamón",     "🥩");
        EMOJIS.put("jamon",     "🥩");
        EMOJIS.put("huevos",    "🥚");
        EMOJIS.put("aceite",    "🫒");
        EMOJIS.put("café",      "☕");
        EMOJIS.put("cafe",      "☕");
    }

    @Override
    public void start(Stage stage) {
        root = new StackPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        showMenu();

        Scene scene = new Scene(root, 660, 540);
        stage.setTitle("Tienda Online");
        stage.setScene(scene);
        stage.setMinWidth(480);
        stage.setMinHeight(400);
        stage.show();
    }

    private void showMenu()    { setView(buildMenuPane());    }
    private void showCatalog() { setView(buildCatalogPane()); }
    private void showCart()    { setView(buildCartPane());    }

    private void setView(Node view) {
        root.getChildren().setAll(view);
    }

    private VBox buildMenuPane() {
        VBox vbox = new VBox(12);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40));

        Label title = new Label("🛒 Tienda Online");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        VBox.setMargin(title, new Insets(0, 0, 20, 0));

        Button catalogBtn = menuButton("🛍️  Ver Catálogo", ACCENT);
        catalogBtn.setOnAction(e -> showCatalog());

        Button cartBtn = menuButton("🛒  Ver Carrito", ACCENT);
        cartBtn.setOnAction(e -> showCart());

        Button payBtn = menuButton("💳  Pagar", SUCCESS);
        payBtn.setOnAction(e -> doPay());

        Button exitBtn = menuButton("🚪  Salir", DANGER);
        exitBtn.setOnAction(e -> System.exit(0));

        vbox.getChildren().addAll(title, catalogBtn, cartBtn, payBtn, exitBtn);
        return vbox;
    }

    private BorderPane buildCatalogPane() {
        BorderPane bp = styledBorderPane();

        Label title = centeredTitle("🛍️ Catálogo de Productos");
        BorderPane.setMargin(title, new Insets(0, 0, 14, 0));
        bp.setTop(title);

        VBox list = new VBox(8);
        list.setPadding(new Insets(4, 0, 4, 0));
        list.setStyle("-fx-background-color: " + BG + ";");

        for (Product p : catalog.getAvailableProducts()) {
            list.getChildren().add(productCard(p));
        }

        bp.setCenter(styledScroll(list));

        Button back = actionButton("← Volver al Menú", ACCENT);
        back.setOnAction(e -> showMenu());
        bp.setBottom(centeredRow(back));

        return bp;
    }

    private HBox productCard(Product p) {
        HBox card = cardHBox();

        Label name = new Label(getEmoji(p.getName()) + "  " + p.getName());
        name.setFont(Font.font("Segoe UI", 15));
        HBox.setHgrow(name, Priority.ALWAYS);

        Label price = priceLabel(p.getPrice());

        Button add = smallButton("+ Añadir", SUCCESS);
        add.setOnAction(e -> {
            cart.addProduct(catalog.getProductById(p.getId()));
            showToast("✓  Añadido: " + p.getName());
        });

        card.getChildren().addAll(name, price, add);
        return card;
    }

    private BorderPane buildCartPane() {
        BorderPane bp = styledBorderPane();

        Label title = centeredTitle("🛒 Tu Carrito");
        BorderPane.setMargin(title, new Insets(0, 0, 14, 0));
        bp.setTop(title);

        bp.setCenter(styledScroll(buildCartBody()));

        Button back = actionButton("← Volver al Menú", ACCENT);
        back.setOnAction(e -> showMenu());

        Button pay = actionButton("💳  Pagar", SUCCESS);
        pay.setOnAction(e -> doPay());

        HBox south = centeredRow(back, pay);
        south.setSpacing(14);
        bp.setBottom(south);

        return bp;
    }

    private VBox buildCartBody() {
        VBox body = new VBox(8);
        body.setPadding(new Insets(4, 0, 4, 0));
        body.setStyle("-fx-background-color: " + BG + ";");

        if (cart.isEmpty()) {
            Label empty = new Label("El carrito está vacío.");
            empty.setFont(Font.font("Segoe UI", 15));
            empty.setStyle("-fx-text-fill: #6c757d;");
            body.setAlignment(Pos.CENTER);
            body.getChildren().add(empty);
            return body;
        }

        for (Map.Entry<Integer, Product> entry : cart.getProducts().entrySet()) {
            int     id  = entry.getKey();
            Product p   = entry.getValue();
            int     qty = cart.getQuantities().get(id);
            body.getChildren().add(cartCard(p, qty));
        }

        Label total = new Label(String.format("Total: %.2f €", cart.getTotal()));
        total.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        total.setStyle("-fx-text-fill: " + SUCCESS + ";");
        VBox.setMargin(total, new Insets(10, 4, 2, 0));
        body.getChildren().add(total);

        return body;
    }

    private HBox cartCard(Product p, int qty) {
        HBox card = cardHBox();

        Label info = new Label(getEmoji(p.getName()) + "  " + p.getName() + " ×" + qty);
        info.setFont(Font.font("Segoe UI", 14));
        HBox.setHgrow(info, Priority.ALWAYS);

        Label price = priceLabel(p.getPrice() * qty);

        Button del = new Button("−");
        del.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        del.setStyle("-fx-background-color: " + DANGER + "; -fx-text-fill: white; " +
                     "-fx-cursor: hand; -fx-background-radius: 4; " +
                     "-fx-min-width: 34; -fx-min-height: 34; -fx-max-width: 34; -fx-max-height: 34;");
        del.setOnMouseEntered(ev -> del.setOpacity(0.80));
        del.setOnMouseExited(ev ->  del.setOpacity(1.0));
        del.setOnAction(ev -> {
            cart.removeProduct(p.getId());
            showCart();
        });

        card.getChildren().addAll(info, price, del);
        return card;
    }

    private void doPay() {
        if (cart.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Carrito vacío");
            alert.setHeaderText(null);
            alert.setContentText("El carrito está vacío.\nAñade productos antes de pagar.");
            alert.showAndWait();
            return;
        }
        cart.checkout(invoicesDir);
        showMenu();
    }

    private BorderPane styledBorderPane() {
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: " + BG + ";");
        bp.setPadding(new Insets(20, 24, 16, 24));
        return bp;
    }

    private Label centeredTitle(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        return lbl;
    }

    private HBox cardHBox() {
        HBox card = new HBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10, 14, 10, 14));
        card.setStyle("-fx-background-color: " + WHITE + "; " +
                      "-fx-border-color: #dee2e6; " +
                      "-fx-border-radius: 6; -fx-background-radius: 6;");
        return card;
    }

    private Label priceLabel(double amount) {
        Label lbl = new Label(String.format("%.2f €", amount));
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-text-fill: " + ACCENT + ";");
        return lbl;
    }

    private ScrollPane styledScroll(Node content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + "; " +
                        "-fx-border-color: transparent;");
        return scroll;
    }

    private HBox centeredRow(Node... nodes) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(12, 0, 0, 0));
        row.getChildren().addAll(nodes);
        return row;
    }

    private Button menuButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        btn.setPrefWidth(260);
        btn.setPrefHeight(46);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                     "-fx-cursor: hand; -fx-background-radius: 6;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.82));
        btn.setOnMouseExited(e ->  btn.setOpacity(1.0));
        return btn;
    }

    private Button actionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btn.setPrefWidth(190);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                     "-fx-cursor: hand; -fx-background-radius: 6;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.82));
        btn.setOnMouseExited(e ->  btn.setOpacity(1.0));
        return btn;
    }

    private Button smallButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                     "-fx-cursor: hand; -fx-background-radius: 4;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.82));
        btn.setOnMouseExited(e ->  btn.setOpacity(1.0));
        return btn;
    }

    private String getEmoji(String name) {
        String lower = name.toLowerCase();
        for (Map.Entry<String, String> e : EMOJIS.entrySet()) {
            if (lower.contains(e.getKey())) return e.getValue();
        }
        return "📦";
    }

    private void showToast(String message) {
        Label toast = new Label(message);
        toast.setPadding(new Insets(10, 20, 10, 20));
        toast.setFont(Font.font("Segoe UI", 13));
        toast.setStyle("-fx-background-color: rgba(40,40,40,0.88); " +
                       "-fx-text-fill: white; -fx-background-radius: 20;");

        root.getChildren().add(toast);
        StackPane.setAlignment(toast, Pos.BOTTOM_CENTER);
        StackPane.setMargin(toast, new Insets(0, 0, 40, 0));

        FadeTransition fade = new FadeTransition(Duration.millis(500), toast);
        fade.setDelay(Duration.millis(1400));
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> root.getChildren().remove(toast));
        fade.play();
    }
}