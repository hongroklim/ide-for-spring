package dev.rokong.order.delivery;

import dev.rokong.dto.OrderDeliveryDTO;

public interface OrderDeliveryService {
    public OrderDeliveryDTO getODelivery(int orderId);
    public OrderDeliveryDTO getODeliveryNotNull(int orderId);
    public OrderDeliveryDTO createODelivery(OrderDeliveryDTO oDelivery);
    public OrderDeliveryDTO updateODelivery(OrderDeliveryDTO oDelivery);
    public void deleteODelivery(int orderId);
}