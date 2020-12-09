package dev.rokong.order.product.delivery;

public interface OrderProductDeliveryService {
    public boolean addOPDelivery(int orderId, int deliveryId);
    
    /**
     * 
     * 
     * @param orderId
     * @param deliveryId
     * @return if true, delivery is removed
     */
    public boolean removeOPDelivery(int orderId, int deliveryId);

    public int totalDeliveryPrice(int orderId);
}