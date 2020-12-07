package dev.rokong.product.delivery;

import dev.rokong.dto.ProductDeliveryDTO;

public interface ProductDeliveryService {
    public ProductDeliveryDTO getPDelivery(int id);
    public ProductDeliveryDTO getPDeliveryNotNull(int id);
    public ProductDeliveryDTO initDefaultPDelivery(String sellerNm, int price);
    public void deletePDelivery(int id);
}