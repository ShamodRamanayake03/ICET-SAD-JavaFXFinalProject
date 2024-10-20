package controller.report;
import controller.report.ReportGenerator;
import entity.OrderEntity;
import entity.ProductEntity;
import entity.SupplierEntity;
import entity.UserEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.util.List;

public class ReportController {


    private ReportGenerator reportGenerator = new ReportGenerator();
/**
    @FXML
    void btnGenerateInventoryReportOnAction() {
        List<ProductEntity> inventoryList = fetchInventoryData();
        reportGenerator.generateInventoryReport(inventoryList);
    }

    @FXML
    void btnGenerateEmployeeReportOnAction() {
        List<UserEntity> employeeList = fetchEmployeeData();
        reportGenerator.generateEmployeeReport(employeeList);
    }

    @FXML
    void btnGenerateSupplierReportOnAction() {
        List<SupplierEntity> supplierList = fetchSupplierData();
        reportGenerator.generateSupplierReport(supplierList);
    }

**/
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isAdminUser() {
        // Logic to check if current user is admin
        return true;
    }

/**
    @FXML
    void btnGenerateSalesReportOnAction() {
        if (isAdminUser()) {
            List<SalesData> salesDataList = fetchSalesData();
            reportGenerator.generateSalesReport(salesDataList, "Daily");
        } else {
            showAlert(Alert.AlertType.WARNING, "Access Denied", "Only admin can access sales reports.");
        }
    }
    private List<ProductEntity> fetchInventoryData() {

    }

    private List<UserEntity> fetchEmployeeData() {

    }

    private List<SupplierEntity> fetchSupplierData() {

    }

    private List<SalesData> fetchSalesData() {


    }**/
}
