package dev.rokong.product.delivery;

import dev.rokong.dto.ProductDeliveryDTO;

public interface ProductDeliveryService {
    public ProductDeliveryDTO getPDelivery(int id);
    public ProductDeliveryDTO getPDeliveryNotNull(int id);
    public void checkPDeliveryExist(int id);
    public ProductDeliveryDTO createPDelivery(ProductDeliveryDTO pDelivery);
    public ProductDeliveryDTO initDefaultPDelivery(String sellerNm, int price);
    public ProductDeliveryDTO updatePDelivery(ProductDeliveryDTO pDelivery);
    public void deletePDelivery(int id);
}