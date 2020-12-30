package dev.rokong.delivery;

import dev.rokong.dto.DeliveryDTO;

public interface DeliveryDAO {
    public DeliveryDTO select(int orderId);
    public int count(int orderId);
    public void insert(DeliveryDTO delivery);
    public void update(DeliveryDTO delivery);
    public void delete(int orderId);
}