package services;

import dao.DAO;
import dao.StoreDAO;
import models.Order;
import models.Product;

import java.util.ArrayList;

public class StoreService {
    private static DAO dao = new StoreDAO();

    public static int addCustomer(String name, String email, String phone) {
        return dao.addCustomer(name, email, phone);
    }

    public static int addProduct(String name, double price, int quantity, String description) {
        return dao.addProduct(name, price, quantity, description);
    }

    public static ArrayList<Integer> getCustomer(String searchName) {
        return dao.getCustomer(searchName);
    }

    public static ArrayList<Product> getInventory() {
        return dao.getInventory();
    }

    public static ArrayList<Order> getOrderHistory(int customerId) {
        return dao.getOrderHistory(customerId);
    }

    public static int newOrder(int customerId) {
        return dao.newOrder(customerId);
    }

    public static int addToOrder(int order, int product, int quantity) {
        return dao.addToOrder(order, product, quantity);
    }

    public static double getOrderTotal(int orderId) {
        return dao.getOrderTotal(orderId);
    }

    public static boolean updateProductQuantity(int productId, int newQuantity) {
        return (dao.updateProductQuantity(productId, newQuantity));
    }
}
