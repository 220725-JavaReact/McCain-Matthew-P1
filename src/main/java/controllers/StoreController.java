package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Customer;
import models.LineItem;
import models.Order;
import models.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.StoreService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class StoreController extends HttpServlet {
    final private static ObjectMapper mapper = new ObjectMapper();
    final private Logger logger = LogManager.getLogger(StoreController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String URI = req.getRequestURI().replace("/", "");
        resp.setContentType("application/json");
        String jsonString;

        switch (URI) {
            case "inventory":
                ArrayList<Product> inventory = StoreService.getInventory();
                if (!inventory.isEmpty()) {
                    resp.setStatus(200);
                    jsonString = mapper.writeValueAsString(inventory);
                    resp.getWriter().println(jsonString);
                    logger.info("Retrieved Inventory");
                } else {
                    resp.setStatus(204);
                    logger.warn("No Inventory Found");
                }
                break;
            case "searchCustomer":
                String searchName = req.getParameter("searchName");
                ArrayList<Integer> customersFound = StoreService.getCustomer(searchName);
                if (!customersFound.isEmpty()) {
                    resp.setStatus(200);
                    jsonString = mapper.writeValueAsString(customersFound);
                    resp.getWriter().println(jsonString);
                    logger.info("Found Customers");
                } else {
                    resp.setStatus(204);
                    logger.info("Customer: " + searchName + " NOT FOUND");
                }
                break;
            case "orderHistory":
                int historyCustomerId = Integer.parseInt(req.getParameter("customerId"));
                ArrayList<Order> orders = StoreService.getOrderHistory(historyCustomerId);
                if (!orders.isEmpty()) {
                    resp.setStatus(200);
                    jsonString = mapper.writeValueAsString(orders);
                    resp.getWriter().println(jsonString);
                    logger.info("Found history for customerId: " + historyCustomerId);
                } else {
                    resp.setStatus(204);
                    logger.info("Could not find order history for customerId: " + historyCustomerId);
                }
                break;
            default:
                super.doGet(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String URI = req.getRequestURI().replace("/", "");
        resp.setContentType("application/json");
        String jsonString;

        switch (URI) {
            case "addCustomer":
                String customerName = req.getParameter("name");
                String customerEmail = req.getParameter("email");
                String customerPhone = req.getParameter("phone");
                int customerId = StoreService.addCustomer(customerName, customerEmail, customerPhone);
                if (customerId > 0) {
                    resp.setStatus(200);
                    resp.getWriter().println("{\"customer_id\":\"" + customerId + "\"}");
                    logger.debug("Added a new customer");
                } else {
                    resp.setStatus(400);
                    Customer badCustomer = new Customer(0,"Customer Name", "Customer Email", "Customer Phone");
                    jsonString = mapper.writeValueAsString(badCustomer);
                    resp.getWriter().println(jsonString);
                    logger.info("Unable to add a Customer");
                }
                break;
            case "addProduct":
                String productName = req.getParameter("name");
                double productPrice = Double.parseDouble(req.getParameter("price"));
                int productQuantity = Integer.parseInt(req.getParameter("quantity"));
                String productDescription = req.getParameter("description");
                int productId = StoreService.addProduct(productName, productPrice, productQuantity, productDescription);
                if (productId > 0) {
                    resp.setStatus(201);
                    jsonString = mapper.writeValueAsString(Integer.toString(productId));
                    resp.getWriter().println(jsonString);
                    logger.info("Added product with id: " + productId);
                } else {
                    resp.setStatus(400);
                    jsonString = mapper.writeValueAsString(Integer.toString(productId));
                    resp.getWriter().println(jsonString);
                    logger.info("Unable to add a new product");
                }
                break;
            case "newOrder":
                int customerId2 = Integer.parseInt(req.getParameter("customerId"));
                int orderId = StoreService.newOrder(customerId2);
                if (orderId > 0) {
                    resp.setStatus(200);
                    jsonString = mapper.writeValueAsString(Integer.toString(orderId));
                    resp.getWriter().println(jsonString);
                    logger.info("Placed a new order with id: " + orderId);
                } else {
                    resp.setStatus(400);
                    jsonString = mapper.writeValueAsString(orderId);
                    resp.getWriter().println(jsonString);
                    logger.info("Unable to create a new order");
                }
                break;
            case "addToOrder":
                int addOrderId = Integer.parseInt(req.getParameter("order"));
                int addProductId = Integer.parseInt(req.getParameter("product"));
                int addQuantity = Integer.parseInt(req.getParameter("quantity"));
                int lineItemId = StoreService.addToOrder(addOrderId, addProductId, addQuantity);
                if (lineItemId > 0) {
                    resp.setStatus(201);
                    jsonString = mapper.writeValueAsString(Integer.toString(lineItemId));
                    resp.getWriter().println(jsonString);
                    logger.info("Added Line Item with id: " + lineItemId);
                } else {
                    resp.setStatus(400);
                    jsonString = mapper.writeValueAsString(Integer.toString(lineItemId));
                    resp.getWriter().println(jsonString);
                    logger.info("Unable to add to order");
                }
                break;
            case "replenishInventory":
                int replenishProductId = Integer.parseInt(req.getParameter("product"));
                int replenishQuantity = Integer.parseInt(req.getParameter("quantity"));
                if (StoreService.updateProductQuantity(replenishProductId, replenishQuantity)) {
                    resp.setStatus(200);
                    logger.info("Updated quantity for productId: " + replenishProductId);
                } else {
                    resp.setStatus(400);
                    logger.info("Unable to update quantity of productId: " + replenishProductId);
                }
                break;
            default:
                super.doPost(req, resp);
                break;
        }
    }
}
