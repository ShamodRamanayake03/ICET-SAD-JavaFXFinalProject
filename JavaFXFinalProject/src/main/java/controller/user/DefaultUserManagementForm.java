package controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultUserManagementForm {

    @FXML
    void btnOrderManagementFormOnAction(ActionEvent event) {
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/order_management_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.show();
        primaryStage.setResizable(false);
    }
    @FXML
    void btnProductManagementFormOnAction(ActionEvent event) {
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/product_management_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    @FXML
    void btnSupplierManagementFormOnAction(ActionEvent event) {
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/supplier_management_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    @FXML
    void btnUserManagementFormOnAction(ActionEvent event) {

    }

}
