package controller.user;

import com.jfoenix.controls.JFXTextField;
import entity.UserEntity;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserLoginFormController {

    @FXML
    private ComboBox<String> cmbAccountType;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPassword;

    private UserController userRepository = new UserController();
    private PauseTransition pauseTransition;

    @FXML
    void initialize() {
        ObservableList<String> categoryList = FXCollections.observableArrayList();
        categoryList.add("Admin");
        categoryList.add("Default");
        cmbAccountType.setItems(categoryList);


        txtName.setOnKeyReleased(this::handleNameInput);
    }

    private void handleNameInput(KeyEvent event) {
        if (pauseTransition != null) {
            pauseTransition.playFromStart();
        } else {
            pauseTransition = new PauseTransition(Duration.millis(500));
        }

        pauseTransition.setOnFinished(e -> {
            String name = txtName.getText();
            UserEntity user = userRepository.getUserByName(name);

            if (user != null) {
                txtId.setText(String.valueOf(user.getId()));
                cmbAccountType.setValue(user.getAccountType());
            } else {
                txtId.clear();
                cmbAccountType.setValue(null);
            }
        });

        pauseTransition.play();
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        long id = Long.parseLong(txtId.getText());
        String inputPassword = txtPassword.getText();
        String accountType = cmbAccountType.getValue();

        UserEntity user = userRepository.getUserById(id);

        if (user != null) {
            if (user.getPassword().equals(encryptPassword(inputPassword)) && user.getAccountType().equals(accountType)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");

                Stage stage = (Stage) txtId.getScene().getWindow();
                stage.close();

                if (accountType.equals("Admin")) {
                    openAdminDashboard();
                } else if (accountType.equals("Default")) {
                    openUserDashboard();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials or account type.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "User not found.");
        }
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

    private void openAdminDashboard() {
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/admin_dashboard_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void openUserDashboard() {
        Stage primaryStage = new Stage();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../../view/default_user_dashboard_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
