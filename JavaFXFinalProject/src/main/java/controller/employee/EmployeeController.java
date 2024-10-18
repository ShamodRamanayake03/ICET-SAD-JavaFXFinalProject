package controller.employee;

import entity.EmployeeEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class EmployeeController implements EmployeeService {
	boolean isSuccess = false;

	@Override
	public boolean saveEmployee(EmployeeEntity employee) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();  // Start transaction
			session.save(employee);  // Persist employee entity
			transaction.commit();  // Commit transaction
			isSuccess = true;  // Set success flag
			showAlert(AlertType.INFORMATION, "Success", "Employee saved successfully.");
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();  // Rollback on error
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to save employee.");
		} finally {
			session.close();  // Ensure session is closed
		}
		return isSuccess;
	}

	@Override
	public boolean updateEmployee(EmployeeEntity employee) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();  // Start transaction
			session.update(employee);  // Update employee entity
			transaction.commit();  // Commit transaction
			isSuccess = true;  // Set success flag
			showAlert(AlertType.INFORMATION, "Success", "Employee updated successfully.");
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();  // Rollback on error
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to update employee.");
		} finally {
			session.close();  // Ensure session is closed
		}
		return isSuccess;
	}

	@Override
	public boolean deleteEmployee(String id) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = null;


		try {
			transaction = session.beginTransaction();  // Start transaction
			EmployeeEntity employee = session.get(EmployeeEntity.class, id);  // Get employee by ID
			if (employee != null) {
				session.delete(employee);  // Delete employee
				transaction.commit();  // Commit transaction
				isSuccess = true;  // Set success flag
				showAlert(AlertType.INFORMATION, "Success", "Employee deleted successfully.");
			} else {
				showAlert(AlertType.WARNING, "Warning", "Employee not found with ID: " + id);
			}
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();  // Rollback on error
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to delete employee.");
		} finally {
			session.close();  // Ensure session is closed
		}
		return isSuccess;
	}

	@Override
	public EmployeeEntity getEmployeeById(String id) {
		Session session = HibernateUtil.getSession();
		EmployeeEntity employee = null;

		try {
			employee = session.get(EmployeeEntity.class, id);  // Get employee by ID
			if (employee == null) {
				showAlert(AlertType.WARNING, "Warning", "No employee found with ID: " + id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to retrieve employee.");
		} finally {
			session.close();  // Ensure session is closed
		}
		return employee;
	}

	@Override
	public List<EmployeeEntity> getAllEmployees() {
		Session session = HibernateUtil.getSession();
		List<EmployeeEntity> employees = null;

		try {
			employees = session.createQuery("from EmployeeEntity", EmployeeEntity.class).list();  // Fetch all employees
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Error", "Failed to load employees.");
		} finally {
			session.close();  // Ensure session is closed
		}
		return employees;
	}

	// Helper method to show alert messages
	private void showAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);  // No header
		alert.setContentText(message);
		alert.showAndWait();
	}
}
