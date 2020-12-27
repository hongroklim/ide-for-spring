package dev.rokong.order.delivery;

import java.util.List;

import dev.rokong.dto.OrderDeliveryDTO;

public interface OrderDeliveryDAO {
    public OrderDeliveryDTO select(OrderDeliveryDTO oPDelivery);
    public void insert(OrderDeliveryDTO oPDelivery);
    public void delete(OrderDeliveryDTO oPDelivery);
    public List<OrderDeliveryDTO> selectByOrder(int orderId);
}