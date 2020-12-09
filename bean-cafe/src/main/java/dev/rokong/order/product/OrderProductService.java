package dev.rokong.order.product;

import java.util.List;

import dev.rokong.dto.OrderProductDTO;

public interface OrderProductService {
    public List<OrderProductDTO> getOProducts(OrderProductDTO oProduct);
    public OrderProductDTO getOProduct(OrderProductDTO oProduct);
    public OrderProductDTO getOProductNotNull(OrderProductDTO oProduct);

    /**
     * add order product
     * 
     * order id must be created before execute this method.
     * productCd is optional.
     * 
     * @param oProduct orderId and productId must be defined
     * @return newly created order product
     */
    public OrderProductDTO addOProduct(OrderProductDTO oProduct);
    public OrderProductDTO updateOProductCnt(OrderProductDTO oProduct);
    public void deleteOProduct(OrderProductDTO oProduct);
    public void updateOProductToNull(int productId, String optionCd);
    public int countOProductsByDelivery(int orderId, int deliveryId);
}