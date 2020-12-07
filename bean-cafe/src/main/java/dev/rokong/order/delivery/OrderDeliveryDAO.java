package dev.rokong.order.delivery;

import dev.rokong.dto.OrderDeliveryDTO;

public interface OrderDeliveryDAO {
    public OrderDeliveryDTO selectOrderDelivery(int orderId);
    public void insertOrderDelivery(OrderDeliveryDTO oDelivery);
    public void updateOrderDelivery(OrderDeliveryDTO oDelivery);
    public void deleteOrderDelivery(int orderId);
}