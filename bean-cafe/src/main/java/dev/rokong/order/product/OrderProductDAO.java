package dev.rokong.order.product;

import java.util.List;

import dev.rokong.dto.OrderProductDTO;

public interface OrderProductDAO {
    public List<OrderProductDTO> selectOProductList(OrderProductDTO oProduct);
    public OrderProductDTO selectOProduct(OrderProductDTO oProduct);
    public void insertOProduct(OrderProductDTO oProduct);
    public void deleteOProduct(OrderProductDTO oProduct);
    public void updateOProductCnt(OrderProductDTO oProduct);
    public void updateOProductToNull(OrderProductDTO oProduct);
    public int countOProductsByDelivery(OrderProductDTO oProduct);
}
