
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class RetailShopInventoryBilling {

    static class Product {
        int id;
        String name;
        double price;
        int stock;

        Product(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }

    static Scanner sc = new Scanner(System.in);
    static Map<Integer, Product> products = new LinkedHashMap<>();

    public static void main(String[] args) {

        // Sample products
        products.put(1, new Product(1, "Rice", 50, 10));
        products.put(2, new Product(2, "Sugar", 40, 10));
        products.put(3, new Product(3, "Milk", 30, 10));

        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println(" RETAIL SHOP MANAGEMENT SYSTEM");
            System.out.println("==================================");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Product");
            System.out.println("3. Restock Product");
            System.out.println("4. Generate Bill");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    viewInventory();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    restockProduct();
                    break;
                case 4:
                    generateBill();
                    break;
                case 5:
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);

        sc.close();
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid whole number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    // Display all products
    static void viewInventory() {
        System.out.println("\n--------- INVENTORY ---------");
        System.out.printf("%-5s %-15s %-10s %-10s%n",
                "ID", "Product", "Price", "Stock");

        for (Product p : products.values()) {
            System.out.printf("%-5d %-15s Rs.%-8.2f %-10d%n",
                    p.id, p.name, p.price, p.stock);
        }
    }

    // Add a new product
    static void addProduct() {
        System.out.print("Enter product ID: ");
        int id = readInt();

        if (products.containsKey(id)) {
            System.out.println("Product ID already exists.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter product name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter product price: ");
        double price = sc.nextDouble();

        System.out.print("Enter opening stock: ");
        int stock = readInt();

        if (name.isEmpty() || price <= 0 || stock < 0) {
            System.out.println("Invalid product details.");
            return;
        }

        products.put(id, new Product(id, name, price, stock));
        System.out.println("Product added successfully!");
    }

    // Increase stock quantity
    static void restockProduct() {
        System.out.print("Enter product ID: ");
        int id = readInt();

        Product p = products.get(id);

        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter quantity to add: ");
        int quantity = readInt();

        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        p.stock += quantity;

        System.out.println("Stock updated successfully.");
        System.out.println("Available stock: " + p.stock);
    }

    // Generate customer bill
    static void generateBill() {
        Map<Integer, Integer> cart = new LinkedHashMap<>();
        double total = 0;

        while (true) {
            viewInventory();

            System.out.print("\nEnter product ID (0 to finish): ");
            int id = readInt();

            if (id == 0) {
                break;
            }

            Product p = products.get(id);

            if (p == null) {
                System.out.println("Product not found.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int quantity = readInt();

            int alreadySelected = cart.getOrDefault(id, 0);

            if (quantity <= 0 ||
                alreadySelected + quantity > p.stock) {
                System.out.println(
                    "Invalid quantity or insufficient stock.");
                continue;
            }

            cart.put(id, alreadySelected + quantity);
            System.out.println("Product added to bill.");
        }

        if (cart.isEmpty()) {
            System.out.println("No products selected.");
            return;
        }

        System.out.println("\n==============================================");
        System.out.println("              CUSTOMER BILL");
        System.out.println("==============================================");
        System.out.printf("%-15s %5s %10s %10s%n",
                "Product", "Qty", "Price", "Amount");

        for (Map.Entry<Integer, Integer> item : cart.entrySet()) {
            Product p = products.get(item.getKey());
            int quantity = item.getValue();
            double amount = p.price * quantity;

            total += amount;

            System.out.printf("%-15s %5d %10.2f %10.2f%n",
                    p.name, quantity, p.price, amount);
        }

        System.out.println("----------------------------------------------");
        System.out.printf("TOTAL AMOUNT: Rs. %.2f%n", total);
        System.out.println("==============================================");

        // Update stock after billing
        for (Map.Entry<Integer, Integer> item : cart.entrySet()) {
            Product p = products.get(item.getKey());
            p.stock -= item.getValue();
        }

        System.out.println("Bill generated successfully!");
        System.out.println("Inventory updated.");
    }
}
