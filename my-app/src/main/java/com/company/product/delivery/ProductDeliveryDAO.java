package com.company.product.delivery;

import java.util.List;

import com.company.dto.ProductDeliveryDTO;

public interface ProductDeliveryDAO {
    public ProductDeliveryDTO selectPDelivery(int id);
    public List<ProductDeliveryDTO> selectPDeliveryBySeller(String sellerNm);
    public ProductDeliveryDTO selectPDeliveryByProductId(int productId);
    public int insertPDelivery(ProductDeliveryDTO pDelivery);
    public void updatePDelivery(ProductDeliveryDTO pDelivery);
    public void deletePDelivery(int id);
}