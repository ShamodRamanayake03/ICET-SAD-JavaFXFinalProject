package controller.supplier;

import com.jfoenix.controls.JFXTextField;
import entity.SupplierEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class SupplierManagementFormController {

    @FXML
    private TableColumn<SupplierEntity, Long> colId;

    @FXML
    private TableColumn<SupplierEntity, String> colCompany;

    @FXML
    private TableColumn<SupplierEntity, String> colEmail;

    @FXML
    private TableColumn<SupplierEntity, String> colName;

    @FXML
    private TableView<SupplierEntity> tblSuppliers;

    @FXML
    private JFXTextField txtCompany;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    private SupplierController supplierRepository = new SupplierController();

    @FXML
    void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadSupplierTable();

        tblSuppliers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                SupplierEntity selectedSupplier = tblSuppliers.getSelectionModel().getSelectedItem();
                if (selectedSupplier != null) {

                    txtId.setText(String.valueOf(selectedSupplier.getId()));
                    txtName.setText(selectedSupplier.getName());
                    txtCompany.setText(selectedSupplier.getCompany());
                    txtEmail.setText(selectedSupplier.getEmail());
                }
            }
        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        SupplierEntity existingSupplier = supplierRepository.getSupplierById(id);

        if (existingSupplier != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Entry");
            alert.setHeaderText(null);
            alert.setContentText("Supplier with ID " + id + " already exists!");
            alert.showAndWait();
        } else {

            SupplierEntity newSupplier = new SupplierEntity();
            newSupplier.setId(Long.valueOf(id));
            newSupplier.setName(txtName.getText());
            newSupplier.setCompany(txtCompany.getText());
            newSupplier.setEmail(txtEmail.getText());

            supplierRepository.saveSupplier(newSupplier);
            loadSupplierTable();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        SupplierEntity updatedSupplier = new SupplierEntity();
        updatedSupplier.setId(Long.valueOf(txtId.getText()));
        updatedSupplier.setName(txtName.getText());
        updatedSupplier.setCompany(txtCompany.getText());
        updatedSupplier.setEmail(txtEmail.getText());

        supplierRepository.updateSupplier(updatedSupplier);
        loadSupplierTable();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        supplierRepository.deleteSupplier(id);
        txtName.clear();
        txtCompany.clear();
        txtEmail.clear();
        loadSupplierTable();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadSupplierTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtId.getText();
        SupplierEntity supplier = supplierRepository.getSupplierById(id);

        if (supplier != null) {

            txtName.setText(supplier.getName());
            txtCompany.setText(supplier.getCompany());
            txtEmail.setText(supplier.getEmail());
        } else {

            System.out.println("Supplier not found with ID: " + id);
            txtName.clear();
            txtCompany.clear();
            txtEmail.clear();
        }
    }


    private void loadSupplierTable() {
        List<SupplierEntity> supplierList = supplierRepository.getAllSuppliers();
        if (supplierList == null) {
            supplierList = new ArrayList<>();
        }
        ObservableList<SupplierEntity> supplierObservableList = FXCollections.observableArrayList(supplierList);
        tblSuppliers.setItems(supplierObservableList);
    }

    public void btnAddItemsOnAction(ActionEvent actionEvent) {
        // TODO document why this method is empty
    }
}
