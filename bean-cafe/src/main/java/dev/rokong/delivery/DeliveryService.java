package dev.rokong.delivery;

import dev.rokong.dto.DeliveryDTO;

public interface DeliveryService {
    public DeliveryDTO getDelivery(int orderId);
    public DeliveryDTO getDeliveryNotNull(int orderId);
    public DeliveryDTO createDelivery(DeliveryDTO delivery);
    public DeliveryDTO updateDelivery(DeliveryDTO delivery);
    public void deleteDelivery(int orderId);
}