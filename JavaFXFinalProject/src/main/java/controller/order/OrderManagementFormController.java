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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class OrderManagementFormController {

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableView<OrderEntity> tblOrders;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPrice;

    private OrderController orderRepository = new OrderController();

    @FXML
    void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("tPrice"));
        loadOrderTable();

        tblOrders.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                OrderEntity selectedOrder = tblOrders.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {

                    txtId.setText(String.valueOf(selectedOrder.getId()));
                    txtName.setText(selectedOrder.getName());
                    txtDate.setText(selectedOrder.getDate());
                    txtPrice.setText(String.valueOf(selectedOrder.getTPrice()));
                }
            }
        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        OrderEntity existingOrder = orderRepository.getOrderById(id);

        if (existingOrder != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Entry");
            alert.setHeaderText(null);
            alert.setContentText("Order with ID " + id + " already exists!");
            alert.showAndWait();
        } else {

            OrderEntity newOrder = new OrderEntity();
            newOrder.setId(Long.valueOf(id));
            newOrder.setName(txtName.getText());
            newOrder.setDate(txtDate.getText());
            newOrder.setTPrice(Double.valueOf(txtPrice.getText()));

            orderRepository.saveOrder(newOrder);
            loadOrderTable();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        OrderEntity updatedOrder = new OrderEntity();
        updatedOrder.setId(Long.valueOf(txtId.getText()));
        updatedOrder.setName(txtName.getText());
        updatedOrder.setDate(txtDate.getText());
        updatedOrder.setTPrice(Double.valueOf(txtPrice.getText()));

        orderRepository.updateOrder(updatedOrder);
        loadOrderTable();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        orderRepository.deleteOrder(id);
        txtName.clear();
        txtDate.clear();
        txtPrice.clear();
        loadOrderTable();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadOrderTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtId.getText();
        OrderEntity order = orderRepository.getOrderById(id);

        if (order != null) {

            txtName.setText(order.getName());
            txtDate.setText(order.getDate());
            txtPrice.setText(String.valueOf(order.getTPrice()));

        } else {

            System.out.println("Order not found with ID: " + id);
            txtName.clear();
            txtDate.clear();
            txtPrice.clear();
        }
    }


    private void loadOrderTable() {
        List<OrderEntity> orderList = orderRepository.getAllOrders();
        ObservableList<OrderEntity> orderObservableList = FXCollections.observableArrayList(orderList);
        tblOrders.setItems(orderObservableList);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/place_order_form.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(loader.load()));


            PlaceOrderFormController placeOrderController = loader.getController();



            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
