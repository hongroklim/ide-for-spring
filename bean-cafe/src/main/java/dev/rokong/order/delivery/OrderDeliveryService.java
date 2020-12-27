package dev.rokong.order.delivery;

import dev.rokong.dto.OrderDeliveryDTO;

public interface OrderDeliveryService {
    
    public OrderDeliveryDTO getODelivery(OrderDeliveryDTO oDelivery);
    public OrderDeliveryDTO getODeliveryNotNull(OrderDeliveryDTO oPDelivery);

    /**
     * 
     * 
     * @param orderId
     * @param deliveryId
     * @return if true, delivery is added
     */
    public boolean addODelivery(int orderId, int deliveryId);
    
    /**
     * 
     * 
     * @param orderId
     * @param deliveryId
     * @return if true, delivery is removed
     */
    public boolean removeODelivery(int orderId, int deliveryId);

    /**
     * calculate 
     * 
     * @param orderId
     * @return
     */
    public int totalDeliveryPrice(int orderId);
}