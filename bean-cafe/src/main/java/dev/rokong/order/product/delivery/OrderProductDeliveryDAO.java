package dev.rokong.order.product.delivery;

import dev.rokong.dto.OrderProductDeliveryDTO;

public interface OrderProductDeliveryDAO {
    public OrderProductDeliveryDTO selectOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public void insertOPDelivery(OrderProductDeliveryDTO oPDelivery);
}