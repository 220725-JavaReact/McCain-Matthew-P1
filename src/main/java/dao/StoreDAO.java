package dao;

import models.Order;
import models.Product;
import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StoreDAO implements DAO {

    public StoreDAO() {
    }

    @Override
    public int addCustomer(String name, String email, String phone) {
        int id = -1;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "insert into customers(name, email, phone) values(?,?,?) returning customer_id";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("customer_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public ArrayList<Integer> getCustomer(String searchName) {
        ArrayList<Integer> customerList = new ArrayList<>();
        searchName = "%" + searchName + "%";
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "select customer_id from customers where name like ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, searchName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                customerList.add(rs.getInt("customer_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    @Override
    public int addProduct(String name, double price, int quantity, String description) {
        int id = -1;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "insert into products(name, price, quantity, description)" +
                    "values(?,?,?,?) returning product_id";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setString(4, description);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("product_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "select * from products";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("description")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public ArrayList<Order> getOrderHistory(int customerId) {
        ArrayList<Order> orders = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "select * from orders where order_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        getOrderTotal(rs.getInt("order_id"))
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public int newOrder(int customerId) {
        int id = -1;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "insert into orders (customer_id) values (?) returning order_id";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("order_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int addToOrder(int order, int product, int quantity) {
        int id = -1;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "insert into line_items(order_id, product_id, quantity) values (?,?,?) returning line_item_id";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, order);
            statement.setInt(2, product);
            statement.setInt(3, quantity);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("line_item_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        subtractProductQuantity(product, quantity);
        return id;
    }

    @Override
    public double getOrderTotal(int orderId) {
        double total = 0;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "select products.price, line_items.quantity from products inner join line_items on products.product_id = line_items.product_id and line_items.order_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                total += rs.getDouble("price") * rs.getInt("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    @Override
    public boolean updateProductQuantity(int productId, int newQuantity) {
        boolean result = false;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "update products set quantity = ? where product_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            statement.execute();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private int subtractProductQuantity(int productId, int adjustment) {
        int newQuantity = getProductQuantity(productId) - adjustment;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "update products set quantity = ? where product_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            if (statement.execute()) {
                return newQuantity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getProductQuantity(int productId) {
        int quantity = -1;
        try (Connection conn = ConnectionUtil.getConn()){
            String query = "select quantity from products where product_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                quantity = rs.getInt("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }
}
