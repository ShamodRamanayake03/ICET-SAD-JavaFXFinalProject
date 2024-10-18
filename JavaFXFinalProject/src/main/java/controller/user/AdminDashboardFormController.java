package controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardFormController {

    @FXML
    void btnEmployeeManagementFormOnAction(ActionEvent event) {
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/employee_management_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    @FXML
    void btnOrderManagementFormOnAction(ActionEvent event) {

    }

    @FXML
    void btnProductManagementFormOnAction(ActionEvent event) {

    }

    @FXML
    void btnReportManagementFormOnAction(ActionEvent event) {

    }

    @FXML
    void btnSupplierManagementFormOnAction(ActionEvent event) {

    }

    @FXML
    void btnUserManagementFormOnAction(ActionEvent event) {

    }

}
