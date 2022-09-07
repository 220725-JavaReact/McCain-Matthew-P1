package models;

import java.util.ArrayList;

public class Order {

    private int id;
    private int customerId;
    private ArrayList<LineItem> items;
    private double total;

    public Order(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    public Order(int id, int customerId, double total) {
        this.id = id;
        this.customerId = customerId;
        items = new ArrayList<>();
        this.total = total;
    }

    public Order(int id, int customerId, ArrayList<LineItem> items, double total) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void addLineItem(LineItem item) {
        items.add(item);
    }
}
