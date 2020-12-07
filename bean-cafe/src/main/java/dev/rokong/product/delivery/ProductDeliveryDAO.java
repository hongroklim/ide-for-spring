package dev.rokong.product.delivery;

import java.util.List;

import dev.rokong.dto.ProductDeliveryDTO;

public interface ProductDeliveryDAO {
    public ProductDeliveryDTO selectPDelivery(int id);
    public List<ProductDeliveryDTO> selectPDeliveryBySeller(String sellerNm);
    public int insertPDelivery(ProductDeliveryDTO pDelivery);
    public void updatePDelivery(ProductDeliveryDTO pDelivery);
    public void deletePDelivery(int id);
}