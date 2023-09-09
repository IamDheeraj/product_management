package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import java.sql.Statement;


public class ProductManagement {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";
    private static int P_id;
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            if (connection != null) {
                System.out.println("Connected to the database");
                // Create a Scanner for user input
                Scanner scanner = new Scanner(System.in);

                while (true) {
                    System.out.println("\nProduct Management Menu:");
                    System.out.println("1. Create Product");
                    System.out.println("2. Read Products");
                    System.out.println("3. Update Product");
                    System.out.println("4. Delete Product");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            createProduct(connection, scanner);
                            break;
                        case 2:
                            readProducts(connection);
                            break;
                        case 3:
                            updateProduct(connection, scanner);
                            break;
                        case 4:
                            deleteProduct(connection, scanner);
                            break;
                        case 5:
                            System.out.println("Exiting the application.");
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Create Product
    private static void createProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Product Name: ");
        String productName = scanner.next();
        System.out.print("Enter product id ");
        P_id=scanner.nextInt();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Category: ");
        String category = scanner.next();

        String insertQuery = "INSERT INTO products (P_id, P_name, Qun, Price, Cate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, P_id);
            preparedStatement.setString(2, productName);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, price);
            preparedStatement.setString(5, category);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Failed to add the product.");
            }
        }
    }

    // Read Products
    private static void readProducts(Connection connection) throws SQLException {
        String selectQuery = "SELECT * FROM products";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int productId = resultSet.getInt("P_id");
                String productName = resultSet.getString("P_name");
                int quantity = resultSet.getInt("Qun");
                double price = resultSet.getDouble("Price");
                String category = resultSet.getString("Cate");

                System.out.println("Product ID: " + productId);
                System.out.println("Product Name: " + productName);
                System.out.println("Quantity: " + quantity);
                System.out.println("Price: " + price);
                System.out.println("Category: " + category);
                System.out.println();
            }
        }
    }

    // Update Product
    private static void updateProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to update: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter New Product Name: ");
        String newProductName = scanner.nextLine();
        System.out.print("Enter New Quantity: ");
        int newQuantity = scanner.nextInt();
        System.out.print("Enter New Price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter New Category: ");
        String newCategory = scanner.nextLine();

        String updateQuery = "UPDATE products SET P_name=?, Qun=?, Price=?, Cate=? WHERE P_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newProductName);
            preparedStatement.setInt(2, newQuantity);
            preparedStatement.setDouble(3, newPrice);
            preparedStatement.setString(4, newCategory);
            preparedStatement.setInt(5, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product with ID " + productId + " not found.");
            }
        }
    }

    // Delete Product
    private static void deleteProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to delete: ");
        int productId = scanner.nextInt();

        String deleteQuery = "DELETE FROM products WHERE P_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product with ID " + productId + " not found.");
            }
        }
    }
}

