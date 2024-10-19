package controller.order;

import entity.OrderEntity;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class OrderController implements OrderService{

    boolean isSuccess = false;

    @Override
    public boolean saveOrder(OrderEntity order) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order saved successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save order.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public boolean updateOrder(OrderEntity order) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(order);
            transaction.commit();
            isSuccess = true;
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully.");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update order.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public boolean deleteOrder(String id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;


        try {
            transaction = session.beginTransaction();
            OrderEntity order = session.get(OrderEntity.class, id);
            if (order != null) {
                session.delete(order);
                transaction.commit();
                isSuccess = true;
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Order not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();  // Rollback on error
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete order.");
        } finally {
            session.close();
        }
        return isSuccess;
    }

    @Override
    public OrderEntity getOrderById(String id) {
        Session session = HibernateUtil.getSession();
        OrderEntity order = null;

        try {
            order = session.get(OrderEntity.class, id);
            if (order == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No order found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve order.");
        } finally {
            session.close();
        }
        return order;
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        Session session = HibernateUtil.getSession();
        List<OrderEntity> orders = null;

        try {
            orders = session.createQuery("from OrderEntity", OrderEntity.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load orders.");
        } finally {
            session.close();
        }
        return orders;
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
