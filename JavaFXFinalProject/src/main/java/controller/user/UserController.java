package controller.user;

import controller.user.UserService;
import entity.UserEntity;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class UserController implements UserService{

        boolean isSuccess = false;

        @Override
        public boolean saveUser(UserEntity user) {
            Session session = HibernateUtil.getSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();
                isSuccess = true;
                showAlert(Alert.AlertType.INFORMATION, "Success", "User saved successfully.");
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user.");
            } finally {
                session.close();
            }
            return isSuccess;
        }

        @Override
        public boolean updateUser(UserEntity user) {
            Session session = HibernateUtil.getSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(user);
                transaction.commit();
                isSuccess = true;
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user.");
            } finally {
                session.close();
            }
            return isSuccess;
        }

        @Override
        public boolean deleteUser(String id) {
            Session session = HibernateUtil.getSession();
            Transaction transaction = null;


            try {
                transaction = session.beginTransaction();
                UserEntity user = session.get(UserEntity.class, id);
                if (user != null) {
                    session.delete(user);
                    transaction.commit();
                    isSuccess = true;
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warning", "User not found with ID: " + id);
                }
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();  // Rollback on error
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.");
            } finally {
                session.close();
            }
            return isSuccess;
        }

    @Override
    public UserEntity getUserById(String id) {
        Session session = HibernateUtil.getSession();
        UserEntity user = null;

        try {
            user = session.get(UserEntity.class, id);
            if (user == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve user.");
        } finally {
            session.close();
        }
        return user;
    }


    public UserEntity getUserById(long id) {
        Session session = HibernateUtil.getSession();
        UserEntity user = null;

        try {
            user = session.get(UserEntity.class, id);
            if (user == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve user.");
        } finally {
            session.close();
        }
        return user;
    }

    public UserEntity getUserByName(String name) {
        Session session = HibernateUtil.getSession();
        UserEntity user = null;

        try {
            user = session.createQuery("from UserEntity where name = :name", UserEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();
            if (user == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "No user found with name: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve user.");
        } finally {
            session.close();
        }
        return user;
    }


    @Override
        public List<UserEntity> getAllUsers() {
            Session session = HibernateUtil.getSession();
            List<UserEntity> users = null;

            try {
                users = session.createQuery("from UserEntity", UserEntity.class).list();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load users.");
            } finally {
                session.close();
            }
            return users;
        }


        private void showAlert(Alert.AlertType alertType, String title, String message) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
