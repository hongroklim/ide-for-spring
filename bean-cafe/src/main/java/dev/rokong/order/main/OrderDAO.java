package dev.rokong.order.main;

import dev.rokong.dto.OrderDTO;

public interface OrderDAO {
    public OrderDTO select(int id);
    public int insert(OrderDTO order);
    public void updatePay(OrderDTO order);
    public void updateOrderStatus(OrderDTO order);
    public void updatePrice(OrderDTO order);
    public void updateDeliveryPrice(OrderDTO order);
}
