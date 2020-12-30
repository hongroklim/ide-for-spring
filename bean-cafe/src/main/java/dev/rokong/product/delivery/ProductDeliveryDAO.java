package dev.rokong.product.delivery;

import java.util.List;

import dev.rokong.dto.ProductDeliveryDTO;

public interface ProductDeliveryDAO {
    public ProductDeliveryDTO select(int id);
    public int count(int id);
    public int insert(ProductDeliveryDTO pDelivery);
    public void update(ProductDeliveryDTO pDelivery);
    public void delete(int id);
}