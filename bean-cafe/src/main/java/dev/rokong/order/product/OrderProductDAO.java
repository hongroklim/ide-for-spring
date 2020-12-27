package dev.rokong.order.product;

import dev.rokong.dto.OrderProductDTO;

import java.util.List;

public interface OrderProductDAO {
    public List<OrderProductDTO> selectList(OrderProductDTO oProduct);
    public OrderProductDTO select(OrderProductDTO oProduct);
    public void insert(OrderProductDTO oProduct);
    public void delete(OrderProductDTO oProduct);
    public void updateCnt(OrderProductDTO oProduct);
    public void updateToNull(OrderProductDTO oProduct);
    public int countByDelivery(OrderProductDTO oProduct);
    public void updateStatus(OrderProductDTO oProduct);
}