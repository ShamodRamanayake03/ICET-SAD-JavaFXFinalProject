package controller.order;

import com.jfoenix.controls.JFXTextField;
import entity.OrderEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class OrderManagementFormController {

    @FXML
    private TableColumn<OrderEntity, Long> colId;

    @FXML
    private TableColumn<OrderEntity, String> colName;

    @FXML
    private TableColumn<OrderEntity, String> colDate;

    @FXML
    private TableColumn<OrderEntity, Double> colPrice;

    @FXML
    private TableView<OrderEntity> tblOrders;

    @FXML
    private JFXTextField txtId;

    private final OrderController orderController = new OrderController(); // Assume you have an OrderController for order operations
    private ObservableList<OrderEntity> orderList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        loadOrderTable();
    }

    private void loadOrderTable() {
        List<OrderEntity> orders = orderController.getAllOrders();
        orderList = FXCollections.observableArrayList(orders);
        tblOrders.setItems(orderList);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/place_order_form.fxml"))));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load place order form.");
            e.printStackTrace();
        }
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        OrderEntity selectedOrder = tblOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderController.deleteOrder(String.valueOf(selectedOrder.getOrderId())); // Assume you have a method in OrderController to delete an order
            loadOrderTable();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to delete.");
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadOrderTable(); // Reload the order table
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String orderIdText = txtId.getText();
        if (!orderIdText.isEmpty()) {
            Long orderId = Long.valueOf(orderIdText);
            OrderEntity order = orderController.getOrderById(String.valueOf(orderId)); // Assume you have this method
            if (order != null) {
                orderList.clear();
                orderList.add(order); // Display the found order
                tblOrders.setItems(orderList);
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "No order found with that ID.");
                loadOrderTable(); // Reload all orders if not found
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Empty Input", "Please enter an order ID to search.");
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        OrderEntity selectedOrder = tblOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // You can open a new form to update the selected order
            // This would typically involve passing the selected order's details to another form
            // For demonstration, you can simply show a message
            showAlert(Alert.AlertType.INFORMATION, "Update Order", "This functionality is yet to be implemented.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to update.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
