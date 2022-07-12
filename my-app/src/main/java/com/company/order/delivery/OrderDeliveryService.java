package com.company.order.delivery;

import com.company.dto.OrderDeliveryDTO;

public interface OrderDeliveryService {
    public OrderDeliveryDTO getODelivery(int orderId);
    public OrderDeliveryDTO getODeliveryNotNull(int orderId);
    public OrderDeliveryDTO createODelivery(OrderDeliveryDTO oDelivery);
    public OrderDeliveryDTO updateODelivery(OrderDeliveryDTO oDelivery);
    public void deleteODelivery(int orderId);
}