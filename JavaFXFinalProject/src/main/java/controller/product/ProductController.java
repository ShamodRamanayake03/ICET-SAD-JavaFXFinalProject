package controller.product;

import entity.ProductEntity;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ProductController implements ProductService {
    boolean isSuccess = false;

    @Override
    public boolean saveProduct(ProductEntity product) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product saved successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save product.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public boolean updateProduct(ProductEntity product) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(product);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public boolean deleteProduct(String id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;


        try {
            transaction = session.beginTransaction();
            ProductEntity product = session.get(ProductEntity.class, id);
            if (product != null) {
                session.delete(product);
                transaction.commit();
                isSuccess = true;
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Product not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Rollback on error
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete product.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public ProductEntity getProductById(String id) {
        Session session = HibernateUtil.getSession();
        ProductEntity product = null;

        try {
            product = session.get(ProductEntity.class, id);
            if (product == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No product found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve product.");
        } finally {
            session.close();
        }
        return product;
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        Session session = HibernateUtil.getSession();
        List<ProductEntity> products = null;

        try {
            products = session.createQuery("from ProductEntity", ProductEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load products.");
        } finally {
            session.close();
        }
        return products;
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
