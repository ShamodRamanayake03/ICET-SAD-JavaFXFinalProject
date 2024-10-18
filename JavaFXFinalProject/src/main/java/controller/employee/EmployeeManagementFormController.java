package controller.employee;

import com.mysql.cj.xdevapi.Warning;
import entity.EmployeeEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXTextField;


import java.util.List;

public class EmployeeManagementFormController {

    @FXML
    private TableColumn<EmployeeEntity, String> colCompany;

    @FXML
    private TableColumn<EmployeeEntity, String> colEmail;

    @FXML
    private TableColumn<EmployeeEntity, String> colId;

    @FXML
    private TableColumn<EmployeeEntity, String> colName;

    @FXML
    private TableView<EmployeeEntity> tblEmployees;

    @FXML
    private JFXTextField txtCompany;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    private EmployeeController employeeRepository = new EmployeeController();

    @FXML
    void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadEmployeeTable();

        tblEmployees.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                EmployeeEntity selectedEmployee = tblEmployees.getSelectionModel().getSelectedItem();
                if (selectedEmployee != null) {

                    txtId.setText(String.valueOf(selectedEmployee.getId()));
                    txtName.setText(selectedEmployee.getName());
                    txtCompany.setText(selectedEmployee.getCompany());
                    txtEmail.setText(selectedEmployee.getEmail());
                }
            }
        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        EmployeeEntity existingEmployee = employeeRepository.getEmployeeById(id);

        if (existingEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Entry");
            alert.setHeaderText(null);
            alert.setContentText("Employee with ID " + id + " already exists!");
            alert.showAndWait();
        } else {

            EmployeeEntity newEmployee = new EmployeeEntity();
            newEmployee.setId(Long.valueOf(id));
            newEmployee.setName(txtName.getText());
            newEmployee.setCompany(txtCompany.getText());
            newEmployee.setEmail(txtEmail.getText());

            employeeRepository.saveEmployee(newEmployee);
            loadEmployeeTable();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        EmployeeEntity updatedEmployee = new EmployeeEntity();
        updatedEmployee.setId(Long.valueOf(txtId.getText()));
        updatedEmployee.setName(txtName.getText());
        updatedEmployee.setCompany(txtCompany.getText());
        updatedEmployee.setEmail(txtEmail.getText());

        employeeRepository.updateEmployee(updatedEmployee);
        loadEmployeeTable();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        employeeRepository.deleteEmployee(id);
        txtName.clear();
        txtCompany.clear();
        txtEmail.clear();
        loadEmployeeTable();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadEmployeeTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtId.getText();
        EmployeeEntity employee = employeeRepository.getEmployeeById(id);

        if (employee != null) {

            txtName.setText(employee.getName());
            txtCompany.setText(employee.getCompany());
            txtEmail.setText(employee.getEmail());
        } else {

            System.out.println("Employee not found with ID: " + id);
            txtName.clear();
            txtCompany.clear();
            txtEmail.clear();
        }
    }


    private void loadEmployeeTable() {
        List<EmployeeEntity> employeeList = employeeRepository.getAllEmployees();
        ObservableList<EmployeeEntity> employeeObservableList = FXCollections.observableArrayList(employeeList);
        tblEmployees.setItems(employeeObservableList);
    }
}
