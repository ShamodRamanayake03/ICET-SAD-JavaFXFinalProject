package controller.user;

import com.jfoenix.controls.JFXTextField;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserManagementFormController {

    @FXML
    private ComboBox<String> cmbAccountType;

    @FXML
    private TableColumn<UserEntity, String> colAccountType;

    @FXML
    private TableColumn<UserEntity, Long> colId;

    @FXML
    private TableColumn<UserEntity, String> colName;

    @FXML
    private TableColumn<UserEntity, String> colPassword;

    @FXML
    private TableView<UserEntity> tblUsers;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPassword;

    private UserController userRepository = new UserController();

    @FXML
    void initialize() {

        ObservableList<String> categoryList = FXCollections.observableArrayList();
        categoryList.add("Admin");
        categoryList.add("Default");
        cmbAccountType.setItems(categoryList);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colAccountType.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        loadUserTable();


        tblUsers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                UserEntity selectedUser = tblUsers.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {

                    txtId.setText(String.valueOf(selectedUser.getId()));
                    txtName.setText(selectedUser.getName());
                    txtPassword.setText(selectedUser.getPassword());
                    cmbAccountType.setValue(selectedUser.getAccountType());

                }
            }
        });
    }
    
    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        UserEntity existingUser = userRepository.getUserById(id);

        if (existingUser != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Entry");
            alert.setHeaderText(null);
            alert.setContentText("User with ID " + id + " already exists!");
            alert.showAndWait();
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setId(Long.valueOf(id));
            newUser.setName(txtName.getText());
            newUser.setPassword(encryptPassword(txtPassword.getText()));
            newUser.setAccountType(cmbAccountType.getValue());
            userRepository.saveUser(newUser);
            loadUserTable();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        userRepository.deleteUser(id);
        txtName.clear();
        txtPassword.clear();

        loadUserTable();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadUserTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtId.getText();
        UserEntity user = userRepository.getUserById(id);

        if (user != null) {
            txtName.setText(user.getName());
            txtPassword.setText(user.getPassword());
            cmbAccountType.setValue(user.getAccountType());

        } else {
            System.out.println("User not found with ID: " + id);
            txtName.clear();
            txtPassword.clear();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(Long.valueOf(txtId.getText()));
        updatedUser.setName(txtName.getText());
        updatedUser.setPassword(encryptPassword(txtPassword.getText()));
        updatedUser.setAccountType(cmbAccountType.getValue());

        userRepository.updateUser(updatedUser);
        loadUserTable();
    }

    private void loadUserTable() {
        List<UserEntity> userList = userRepository.getAllUsers();
        ObservableList<UserEntity> userObservableList = FXCollections.observableArrayList(userList);
        tblUsers.setItems(userObservableList);
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
