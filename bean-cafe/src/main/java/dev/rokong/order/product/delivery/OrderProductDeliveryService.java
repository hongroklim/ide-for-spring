package dev.rokong.order.product.delivery;

import dev.rokong.dto.OrderProductDeliveryDTO;

public interface OrderProductDeliveryService {
    public OrderProductDeliveryDTO addOPDelivery(int orderId, int productId);
    /**
     * 
     * 
     * @param orderId
     * @param deliveryId
     * @return if true, delivery is removed
     */
    public boolean removeOPDelivery(int orderId, int deliveryId);
}