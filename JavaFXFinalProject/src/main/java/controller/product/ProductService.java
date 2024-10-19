package controller.product;

import entity.ProductEntity;

import java.util.List;

public interface ProductService {
    boolean saveProduct(ProductEntity product);
    boolean updateProduct(ProductEntity product);
    boolean deleteProduct(String id);
    ProductEntity getProductById(String id);
    List<ProductEntity> getAllProducts();
}
