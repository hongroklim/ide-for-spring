package com.company.order.main;

import com.company.dto.OrderDTO;

public interface OrderService {
    public OrderDTO getOrder(int id);
    public OrderDTO getOrderNotNull(int id);
    public OrderDTO initOrder(OrderDTO order);
    public void updateOrderPrice(int id, int price, int deliveryPrice);
    public void updateOrderPrice(int id, int price);
    public void updateOrderDeliveryPrice(int id, int deliveryPrice);
    public void updateOrderPay(OrderDTO order);
    public OrderDTO updateOrderStatus(OrderDTO order);
    public void cancelOrder(int id, String user);
}
