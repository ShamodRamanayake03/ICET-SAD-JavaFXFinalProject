package controller.supplier;

import entity.SupplierEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

public class SupplierController implements SupplierService {
    boolean isSuccess = false;

    @Override
    public boolean saveSupplier(SupplierEntity supplier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(supplier);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "supplier saved successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save supplier.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public boolean updateSupplier(SupplierEntity supplier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(supplier);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier updated successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update supplier.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public boolean deleteSupplier(String id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;


        try {
            transaction = session.beginTransaction();
            SupplierEntity supplier = session.get(SupplierEntity.class, id);
            if (supplier != null) {
                session.delete(supplier);
                transaction.commit();
                isSuccess = true;
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier deleted successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Supplier not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Rollback on error
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete supplier.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public SupplierEntity getSupplierById(String id) {
        Session session = HibernateUtil.getSession();
        SupplierEntity supplier = null;

        try {
            supplier = session.get(SupplierEntity.class, id);
            if (supplier == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No supplier found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve supplier.");
        } finally {
            session.close();
        }
        return supplier;
    }

    @Override
    public List<SupplierEntity> getAllSuppliers() {
        Session session = HibernateUtil.getSession();
        List<SupplierEntity> suppliers = new ArrayList<>();  // Initialize an empty list

        try {
            suppliers = session.createQuery("from SupplierEntity", SupplierEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            // Log the error, but don't show an alert here, keep it to the UI layer
        } finally {
            if (session != null) {
                session.close();  // Ensure session is closed in any case
            }
        }
        return suppliers;  // This will return an empty list if something went wrong
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
