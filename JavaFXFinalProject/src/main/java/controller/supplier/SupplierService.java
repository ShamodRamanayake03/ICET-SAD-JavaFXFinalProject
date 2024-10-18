package controller.supplier;

import entity.SupplierEntity;

import java.util.List;

public interface SupplierService {
    boolean saveSupplier(SupplierEntity supplier);
    boolean updateSupplier(SupplierEntity supplier);
    boolean deleteSupplier(String id);
    SupplierEntity getSupplierById(String id);
    List<SupplierEntity> getAllSuppliers();
}
