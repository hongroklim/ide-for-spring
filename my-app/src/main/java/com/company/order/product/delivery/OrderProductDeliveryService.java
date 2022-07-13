package com.company.order.product.delivery;

import com.company.dto.OrderProductDeliveryDTO;

public interface OrderProductDeliveryService {
    
    public OrderProductDeliveryDTO getOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public OrderProductDeliveryDTO getOPDeliveryNotNull(OrderProductDeliveryDTO oPDelivery);

    /**
     * 
     * 
     * @param orderId
     * @param deliveryId
     * @return if true, delivery is added
     */
    public boolean addOPDelivery(int orderId, int deliveryId);
    
    /**
     * 
     * 
     * @param orderId
     * @param deliveryId
     * @return if true, delivery is removed
     */
    public boolean removeOPDelivery(int orderId, int deliveryId);

    /**
     * calculate 
     * 
     * @param orderId
     * @return
     */
    public int totalDeliveryPrice(int orderId);
}