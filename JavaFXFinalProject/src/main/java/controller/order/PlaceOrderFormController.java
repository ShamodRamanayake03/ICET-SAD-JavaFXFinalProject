package controller.order;

import com.jfoenix.controls.JFXTextField;
import controller.product.ProductController;
import entity.OrderDetailEntity;
import entity.ProductEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class PlaceOrderFormController {

    @FXML
    private JFXTextField txtItemName, txtQty, txtPrice, txtTotalPrice, txtItemNo;

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
    private final ProductController productController = new ProductController(); // Product controller instance

    @FXML
    public void initialize() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Calculate nPrice dynamically
        colNPrice.setCellValueFactory(cellData -> {
            double qty = cellData.getValue().getQty();
            double price = cellData.getValue().getPrice();
            return new javafx.beans.property.SimpleDoubleProperty(qty * price).asObject();
        });

        // Add listener for selecting items in the cart
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

        // Add key listener to fetch product details on key release
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

    // Event handler to fetch product details when item number is typed
    private void onItemNoKeyReleased(KeyEvent event) {
        String itemNo = txtItemNo.getText();
        if (!itemNo.isEmpty()) {
            ProductEntity product = productController.getProductById(itemNo);
            if (product != null) {
                txtItemName.setText(product.getName()); // Assuming 'name' is the field in ProductEntity
                txtPrice.setText(String.valueOf(product.getPrice())); // Assuming 'unitPrice' is in ProductEntity
            } else {
                clearProductFields(); // Clear fields if no product is found
            }
        }
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

        orderDetailController.saveOrderDetail(orderDetail);
        loadCartTable();
        clearFields();
    }

    public void btnReloadOnAction(ActionEvent actionEvent) {
        loadCartTable();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        ObservableList<OrderDetailEntity> orderDetails = tblCart.getItems();
        double totalPrice = 0.0;

        for (OrderDetailEntity orderDetail : orderDetails) {
            double nPrice = orderDetail.getQty() * orderDetail.getPrice();
            totalPrice += nPrice;
        }

        txtTotalPrice.setText(String.valueOf(totalPrice));
        showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Order placed successfully! Total Price: " + totalPrice);
    }
}
