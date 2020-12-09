package dev.rokong.order.product.delivery;

import java.util.List;

import dev.rokong.dto.OrderProductDeliveryDTO;

public interface OrderProductDeliveryDAO {
    public OrderProductDeliveryDTO selectOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public void insertOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public void deleteOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public List<OrderProductDeliveryDTO> selectOPdeliveriesByOrder(int orderId);
}