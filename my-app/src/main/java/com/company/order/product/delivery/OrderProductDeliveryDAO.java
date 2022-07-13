package com.company.order.product.delivery;

import java.util.List;

import com.company.dto.OrderProductDeliveryDTO;

public interface OrderProductDeliveryDAO {
    public OrderProductDeliveryDTO selectOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public void insertOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public void deleteOPDelivery(OrderProductDeliveryDTO oPDelivery);
    public List<OrderProductDeliveryDTO> selectOPdeliveriesByOrder(int orderId);
}