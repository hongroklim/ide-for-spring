package dev.rokong.order.product;

import java.util.List;

import dev.rokong.dto.OrderProductDTO;

public interface OrderProductService {
    public List<OrderProductDTO> getOProducts(OrderProductDTO oProduct);
    public OrderProductDTO getOProduct(OrderProductDTO oProduct);
    public OrderProductDTO getOProductNotNull(OrderProductDTO oProduct);
    public OrderProductDTO addOProduct(OrderProductDTO oProduct);
    public OrderProductDTO updateOProduct(OrderProductDTO oProduct);
    public void deleteOProduct(OrderProductDTO oProduct);
    public void updateOProductToNull(int productId, String optionCd);
}
