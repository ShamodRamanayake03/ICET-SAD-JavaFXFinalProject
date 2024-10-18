package controller.employee;

import entity.EmployeeEntity;

import java.util.List;

public interface EmployeeService {
    boolean saveEmployee(EmployeeEntity employee);
    boolean updateEmployee(EmployeeEntity employee);
    boolean deleteEmployee(String id);
    EmployeeEntity getEmployeeById(String id);
    List<EmployeeEntity> getAllEmployees();
}
