package dev.rokong.order.delivery;

import dev.rokong.annotation.OrderStatus;
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
     * calculate total delivery price in order
     * 
     * @param orderId to calculate order
     * @return sum of delivery price
     */
    public int totalDeliveryPrice(int orderId);

    public int totalPrice(int orderId);

    public OrderDeliveryDTO updateShipCd(OrderDeliveryDTO oDelivery);
    public void updateStatusByOrder(int orderId, OrderStatus orderStatus);
    public void updateStatus(int orderId, int deliveryId);
    public OrderStatus getProperOrderStatus(int orderId);
}