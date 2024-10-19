package controller.order;

import entity.OrderEntity;

import java.util.List;

public interface OrderService {
    boolean saveOrder(OrderEntity order);
    boolean updateOrder(OrderEntity order);
    boolean deleteOrder(String id);
    OrderEntity getOrderById(String id);
    List<OrderEntity> getAllOrders();
}
