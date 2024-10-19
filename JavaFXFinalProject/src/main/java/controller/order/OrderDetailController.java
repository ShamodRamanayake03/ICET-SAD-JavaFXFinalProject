
package controller.order;

import entity.OrderDetailEntity;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class OrderDetailController {

    public boolean saveOrderDetail(OrderDetailEntity orderDetail) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        boolean isSuccess = false;
        try {
            transaction = session.beginTransaction();
            session.save(orderDetail);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order detail saved successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save order detail.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    public boolean updateOrderDetail(OrderDetailEntity orderDetail) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        boolean isSuccess = false;
        try {
            transaction = session.beginTransaction();
            session.update(orderDetail);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order detail updated successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order detail.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    public boolean deleteOrderDetail(Long id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        boolean isSuccess = false;
        try {
            transaction = session.beginTransaction();
            OrderDetailEntity orderDetail = session.get(OrderDetailEntity.class, id);
            if (orderDetail != null) {
                session.delete(orderDetail);
                transaction.commit();
                isSuccess = true;
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order detail deleted successfully.");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete order detail.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    public List<OrderDetailEntity> getAllOrderDetails() {
        Session session = HibernateUtil.getSession();
        List<OrderDetailEntity> orderDetails = null;
        try {
            orderDetails = session.createQuery("FROM OrderDetailEntity", OrderDetailEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return orderDetails;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
