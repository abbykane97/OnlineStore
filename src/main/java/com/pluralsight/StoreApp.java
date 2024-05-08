package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoreApp {
    private List<Product> products;
    private List<Product> cart;
    private Scanner scanner;

    public StoreApp() throws FileNotFoundException {
        products = new ArrayList<>();
        cart = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadProductsFromCSV("products.csv"); // Load products directly in the constructor
    }

    private void loadProductsFromCSV(String filename) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|", -1);
                if (data.length >= 4) {
                    String id = data[0];
                    String name = data[1];
                    double price = Double.parseDouble(data[2]);
                    String department = data[3];
                    products.add(new Product(id, name, price, department));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayHomeScreen() {
        System.out.println("Welcome to the Online Store!");
        System.out.println("1. Display Products");
        System.out.println("2. Search Products by Name");
        System.out.println("3. Search Products by ID");
        System.out.println("4. Search Products by Price Range");
        System.out.println("5. Search Products by Department");
        System.out.println("6. Display Cart");
        System.out.println("7. Exit");

        int choice = getUserInput(7);
        switch (choice) {
            case 1:
                displayProducts();
                break;
            case 2:
                searchByName();
                break;
            case 3:
                searchById();
                break;
            case 4:
                searchByPriceRange();
                break;
            case 5:
                searchByDepartment();
                break;
            case 6:
                displayCart();
                break;
            case 7:
                System.out.println("Thank you for shopping with us! Goodbye!");
                System.exit(0);
                break;
        }
    }

    private void searchById() {
        System.out.println("Enter the ID of the product:");
        String id = scanner.next();
        Product foundProduct = findProductById(id);
        if (foundProduct != null) {
            List<Product> result = new ArrayList<>();
            result.add(foundProduct);
            displaySearchResults(result);
        } else {
            System.out.println("Product not found.");
        }
    }

    private void searchByDepartment() {
        System.out.println("Enter the deparment of the product:");
        String deparment = scanner.next();
        List<Product> foundProducts = findProductsByDepartment(deparment);
        displaySearchResults(foundProducts);
    }

    private void searchByPriceRange() {
        System.out.println("Enter the minimum price:");
        double minPrice = scanner.nextDouble();
        System.out.println("Enter the maximum price:");
        double maxPrice = scanner.nextDouble();
        List<Product> foundProducts = findProductsByPriceRange(minPrice, maxPrice);
        displaySearchResults(foundProducts);
    }

    private void searchByName() {
        System.out.println("Enter the name of the product:");
        String name = scanner.next();
        List<Product> foundProducts = findProductsByName(name);
        displaySearchResults(foundProducts);
    }

    private void displayProducts() {
        System.out.println("Products available:");
        for (Product product : products) {
            System.out.println(product.getId() + ". " + product.getName() + " - $" + product.getPrice());
        }
        System.out.println("1. Add to Cart");
        System.out.println("2. Back to Home");

        int choice = getUserInput(2);
        switch (choice) {
            case 1:
                System.out.println("Enter the ID of the product to add to cart:");
                String productId = scanner.next();
                Product selectedProduct = findProductById(productId);
                if (selectedProduct != null) {
                    addProductToCart(selectedProduct);
                    System.out.println("Product added to cart successfully!");
                } else {
                    System.out.println("Invalid product ID.");
                }
                displayHomeScreen();
                break;
            case 2:
                displayHomeScreen();
                break;
        }
    }

    public void displayCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            double totalAmount = 0;
            System.out.println("Cart:");
            for (Product product : cart) {
                System.out.println(product.getName() + " - $" + product.getPrice());
                totalAmount += product.getPrice();
            }
            System.out.println("Total: $" + totalAmount);
        }
        System.out.println("1. Remove Product");
        System.out.println("2. Back to Home");

        int choice = getUserInput(2);
        switch (choice) {
            case 1:
                System.out.println("Enter the ID of the product to remove from cart:");
                String productId = scanner.next();
                Product productToRemove = findProductByIdInCart(productId);
                if (productToRemove != null) {
                    removeProductFromCart(productToRemove);
                    System.out.println("Product removed from cart successfully!");
                } else {
                    System.out.println("Product not found in cart.");
                }
                displayCart();
                break;
            case 2:
                displayHomeScreen();
                break;
        }
    }

    private int getUserInput(int maxChoice) {
        int choice;
        do {
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > maxChoice);
        return choice;
    }

    private Product findProductById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    private Product findProductByIdInCart(String id) {
        for (Product product : cart) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public void addProductToCart(Product product) {
        cart.add(product);
    }

    public void removeProductFromCart(Product product) {
        cart.remove(product);
    }

    public void closeScanner() {
        scanner.close();
    }

    // Method to find products by name
    public List<Product> findProductsByName(String name) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                result.add(product);
            }
        }
        return result;
    }

    // Method to find products by price range
    public List<Product> findProductsByPriceRange(double minPrice, double maxPrice) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                result.add(product);
            }
        }
        return result;
    }

    // Method to find products by department
    public List<Product> findProductsByDepartment(String department) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getDepartment().equalsIgnoreCase(department)) {
                result.add(product);
            }
        }
        return result;
    }
    private void displaySearchResults(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Search Results:");
            for (Product product : products) {
                System.out.println(product.getId() + ". " + product.getName() + " - $" + product.getPrice());
            }
        }
    }
}