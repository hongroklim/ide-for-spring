package com.company.product.delivery;

import com.company.dto.ProductDeliveryDTO;

public interface ProductDeliveryService {
    public ProductDeliveryDTO getPDelivery(int id);
    public ProductDeliveryDTO getPDeliveryNotNull(int id);
    public ProductDeliveryDTO getPDeliveryByProduct(int productId);
    public ProductDeliveryDTO createPDelivery(ProductDeliveryDTO pDelivery);
    public ProductDeliveryDTO initDefaultPDelivery(String sellerNm, int price);
    public ProductDeliveryDTO updatePDelivery(ProductDeliveryDTO pDelivery);
    public void deletePDelivery(int id);
}