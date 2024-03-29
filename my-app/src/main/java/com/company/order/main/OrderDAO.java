package com.company.order.main;

import com.company.dto.OrderDTO;

public interface OrderDAO {
    public OrderDTO selectOrder(int id);
    public int insertOrder(OrderDTO order);
    public void updateOrderPay(OrderDTO order);
    public void updateOrderStatus(OrderDTO order);
    public void updateOrderPrice(OrderDTO order);
    public void updateOrderDeliveryPrice(OrderDTO order);
}
