package controller.product;

import com.jfoenix.controls.JFXTextField;
import entity.ProductEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class ProductManagementFormController {

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private TableColumn<ProductEntity, String> colCategory;

    @FXML
    private TableColumn<ProductEntity, Long> colId;

    @FXML
    private TableColumn<ProductEntity, String> colImageLocation;

    @FXML
    private TableColumn<ProductEntity, String> colName;

    @FXML
    private TableColumn<ProductEntity, Double> colPrice;

    @FXML
    private TableColumn<ProductEntity, Double> colQty;

    @FXML
    private TableColumn<ProductEntity, Double> colSize;

    @FXML
    private ImageView imageViewProduct;

    @FXML
    private TableView<ProductEntity> tblProducts;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtImageLocation;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPrize;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtSize;

    private ProductController productRepository = new ProductController();

    @FXML
    void initialize() {

        ObservableList<String> categoryList = FXCollections.observableArrayList();
        categoryList.add("Gents");
        categoryList.add("Ladies");
        categoryList.add("Kids");
        cmbCategory.setItems(categoryList);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colImageLocation.setCellValueFactory(new PropertyValueFactory<>("image"));

        loadProductTable();


        tblProducts.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                ProductEntity selectedProduct = tblProducts.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {

                    txtId.setText(String.valueOf(selectedProduct.getId()));
                    txtName.setText(selectedProduct.getName());
                    cmbCategory.setValue(selectedProduct.getCategory());
                    txtSize.setText(String.valueOf(selectedProduct.getSize()));
                    txtPrize.setText(String.valueOf(selectedProduct.getPrice()));
                    txtQty.setText(String.valueOf(selectedProduct.getQuantity()));
                    txtImageLocation.setText(selectedProduct.getImage());


                    loadImage(selectedProduct.getImage());
                }
            }
        });
    }

    private void loadImage(String imagePath) {

        if (imagePath != null && !imagePath.trim().isEmpty()) {
            String imageUrl = "file:" + imagePath;
            Image image = new Image(imageUrl, true);
            imageViewProduct.setImage(image);
        } else {
            imageViewProduct.setImage(null);
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        ProductEntity existingProduct = productRepository.getProductById(id);

        if (existingProduct != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Entry");
            alert.setHeaderText(null);
            alert.setContentText("Product with ID " + id + " already exists!");
            alert.showAndWait();
        } else {
            ProductEntity newProduct = new ProductEntity();
            newProduct.setId(Long.valueOf(id));
            newProduct.setName(txtName.getText());
            newProduct.setCategory(cmbCategory.getValue());
            newProduct.setSize(Double.valueOf(txtSize.getText()));
            newProduct.setPrice(Double.valueOf(txtPrize.getText()));
            newProduct.setQuantity(Double.valueOf(txtQty.getText()));
            newProduct.setImage(txtImageLocation.getText());

            productRepository.saveProduct(newProduct);
            loadProductTable();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        productRepository.deleteProduct(id);
        txtName.clear();
        txtSize.clear();
        txtPrize.clear();
        txtQty.clear();
        txtImageLocation.clear();
        loadProductTable();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadProductTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtId.getText();
        ProductEntity product = productRepository.getProductById(id);

        if (product != null) {
            txtName.setText(product.getName());
            cmbCategory.setValue(product.getCategory());
            txtSize.setText(String.valueOf(product.getSize()));
            txtPrize.setText(String.valueOf(product.getPrice()));
            txtQty.setText(String.valueOf(product.getQuantity()));
            txtImageLocation.setText(product.getImage());


            loadImage(product.getImage());
        } else {
            System.out.println("Product not found with ID: " + id);
            txtName.clear();
            txtSize.clear();
            txtPrize.clear();
            txtQty.clear();
            txtImageLocation.clear();
            imageViewProduct.setImage(null);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setId(Long.valueOf(txtId.getText()));
        updatedProduct.setName(txtName.getText());
        updatedProduct.setCategory(cmbCategory.getValue());
        updatedProduct.setSize(Double.valueOf(txtSize.getText()));
        updatedProduct.setPrice(Double.valueOf(txtPrize.getText()));
        updatedProduct.setQuantity(Double.valueOf(txtQty.getText()));
        updatedProduct.setImage(txtImageLocation.getText());

        productRepository.updateProduct(updatedProduct);
        loadProductTable();
    }

    private void loadProductTable() {
        List<ProductEntity> productList = productRepository.getAllProducts();
        ObservableList<ProductEntity> productObservableList = FXCollections.observableArrayList(productList);
        tblProducts.setItems(productObservableList);
    }
}
