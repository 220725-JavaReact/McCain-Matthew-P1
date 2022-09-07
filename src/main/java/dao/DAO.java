package dao;

import models.Order;
import models.Product;

import java.util.ArrayList;

public interface DAO {
    int addCustomer(String name, String email, String phone);
    ArrayList<Integer> getCustomer(String searchName);
    int addProduct(String name, double price, int quantity, String description);
    ArrayList<Product> getInventory();
    ArrayList<Order> getOrderHistory(int customerId);
    int newOrder(int customerId);
    int addToOrder(int order, int product, int quantity);
    double getOrderTotal(int orderId);
    boolean updateProductQuantity(int productId, int newQuantity);
}
