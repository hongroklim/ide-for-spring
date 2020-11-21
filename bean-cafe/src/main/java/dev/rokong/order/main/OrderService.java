package dev.rokong.order.main;

import dev.rokong.dto.OrderDTO;

public interface OrderService {
    public OrderDTO getOrder(int id);
    public OrderDTO getOrderNotNull(int id);
    public OrderDTO createOrder(OrderDTO order);
    public OrderDTO updateOrder(OrderDTO order);
    public void deleteOrder(int id);
}
