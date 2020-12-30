package dev.rokong.order.main;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    public OrderDTO getOrder(int id);
    public OrderDTO getOrderNotNull(int id);
    public OrderDTO createOrder(OrderDTO order);
    public void updateOrderPrice(int id);
    public void updateOrderDeliveryPrice(int id);
    public void updateOrderPay(OrderDTO order);
    public OrderDTO updateOrderStatus(OrderDTO order);
    public void updateOrderStatus(int id);
    public String getOrderDesc(int id);
    public OrderStatus getProperOrderStatus(List<?> list);
}
