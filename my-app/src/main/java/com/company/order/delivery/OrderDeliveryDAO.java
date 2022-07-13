package com.company.order.delivery;

import com.company.dto.OrderDeliveryDTO;

public interface OrderDeliveryDAO {
    public OrderDeliveryDTO selectOrderDelivery(int orderId);
    public void insertOrderDelivery(OrderDeliveryDTO oDelivery);
    public void updateOrderDelivery(OrderDeliveryDTO oDelivery);
    public void deleteOrderDelivery(int orderId);
}