package controller.order;

import com.jfoenix.controls.JFXTextField;  // Use this if you're using JFoenix, otherwise, use TextField
import controller.product.ProductController;
import entity.OrderDetailEntity;
import entity.ProductEntity;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

import java.awt.*;
import java.util.List;

public class PlaceOrderFormController {

    @FXML
    private JFXTextField txtItemName, txtQty, txtPrice, txtTotalPrice, txtItemNo, txtOrderId, txtId;

    @FXML
    private TableView<OrderDetailEntity> tblCart;

    @FXML
    private TableColumn<OrderDetailEntity, Long> colItemId;

    @FXML
    private TableColumn<OrderDetailEntity, String> colItemName;

    @FXML
    private TableColumn<OrderDetailEntity, Double> colQty;

    @FXML
    private TableColumn<OrderDetailEntity, Double> colUPrice;

    @FXML
    private TableColumn<OrderDetailEntity, Double> colNPrice;

    private final OrderDetailController orderDetailController = new OrderDetailController();
    private final ProductController productController = new ProductController();
    private Timeline typingTimer;

    @FXML
    public void initialize() {
        // Set up table columns
        colItemId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colNPrice.setCellValueFactory(cellData -> {
            double qty = cellData.getValue().getQty();
            double price = cellData.getValue().getPrice();
            return new SimpleDoubleProperty(qty * price).asObject();
        });

        // Set table item selection event
        tblCart.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                OrderDetailEntity selectedOrderDetail = tblCart.getSelectionModel().getSelectedItem();
                if (selectedOrderDetail != null) {
                    txtItemNo.setText(String.valueOf(selectedOrderDetail.getId()));
                    txtItemName.setText(selectedOrderDetail.getItemName());
                    txtQty.setText(String.valueOf(selectedOrderDetail.getQty()));
                    txtPrice.setText(String.valueOf(selectedOrderDetail.getPrice()));
                }
            }
        });

        txtItemNo.setOnKeyReleased(this::onItemNoKeyReleased);

        loadCartTable();
    }

    private void loadCartTable() {
        List<OrderDetailEntity> orderDetailsList = orderDetailController.getAllOrderDetails();
        ObservableList<OrderDetailEntity> observableList = FXCollections.observableArrayList(orderDetailsList);
        tblCart.setItems(observableList);
    }

    private void clearFields() {
        txtItemName.clear();
        txtQty.clear();
        txtPrice.clear();
        txtTotalPrice.clear();
        txtItemNo.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void onItemNoKeyReleased(KeyEvent event) {
        // Stop any existing timer
        if (typingTimer != null) {
            typingTimer.stop();
        }

        String itemNo = txtItemNo.getText();
        // Start a new timer for 300 milliseconds
        typingTimer = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            if (!itemNo.isEmpty()) {
                ProductEntity product = productController.getProductById(itemNo);
                if (product != null) {
                    txtItemName.setText(product.getName());
                    txtPrice.setText(String.valueOf(product.getPrice()));
                } else {
                    clearProductFields();
                }
            } else {
                clearProductFields();
            }
        }));
        typingTimer.setCycleCount(1); // Ensure it runs only once
        typingTimer.play();
    }

    private void clearProductFields() {
        txtItemName.clear();
        txtPrice.clear();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        OrderDetailEntity selectedOrderDetail = tblCart.getSelectionModel().getSelectedItem();
        if (selectedOrderDetail != null) {
            orderDetailController.deleteOrderDetail(selectedOrderDetail.getId());
            loadCartTable();
            clearFields();
        } else {
            showAlert(Alert.AlertType.WARNING, "No selection", "Please select an item to delete.");
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        OrderDetailEntity selectedOrderDetail = tblCart.getSelectionModel().getSelectedItem();
        if (selectedOrderDetail != null) {
            try {
                String itemName = txtItemName.getText();
                double qty = Double.parseDouble(txtQty.getText());
                double price = Double.parseDouble(txtPrice.getText());

                selectedOrderDetail.setItemName(itemName);
                selectedOrderDetail.setQty(qty);
                selectedOrderDetail.setPrice(price);

                orderDetailController.updateOrderDetail(selectedOrderDetail);
                loadCartTable();
                tblCart.refresh();

                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Item updated successfully!");

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for quantity and price.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to update.");
        }
    }

    public void btnAddOnAction(ActionEvent actionEvent) {

            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setItemName(txtItemName.getText());
            orderDetail.setQty(Double.valueOf(txtQty.getText()));
            orderDetail.setPrice(Double.valueOf(txtPrice.getText()));
            orderDetail.setOrderNo(Long.valueOf(txtOrderId.getText()));

            orderDetailController.saveOrderDetail(orderDetail);
            loadCartTable();
            clearFields();

    }

    public void btnReloadOnAction(ActionEvent actionEvent) {
        loadCartTable();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        // Order placement logic goes here
    }
}
