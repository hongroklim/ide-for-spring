package dev.rokong.order.product;

import java.util.List;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderProductDTO;
import org.springframework.core.annotation.Order;

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
    public int totalPriceByDelivery(int orderId, int deliveryId);
    public int totalPrice(int orderId);
    public void updateOProductStatus(OrderProductDTO oProduct);

    /**
     * product can not access order main directly.
     * alternatively, invoke {@link dev.rokong.order.delivery.OrderDeliveryService} methods
     * to change order main's values.
     *
     * @param orderId
     * @param orderStatus
     * @deprecated {@link #updateStatusByDelivery(int, int, OrderStatus)} is alternative
     */
    public void updateStatusByOrder(int orderId, OrderStatus orderStatus);

    public void updateStatusByDelivery(int orderId, int deliveryId, OrderStatus orderStatus);

    public OrderStatus getProperOrderStatus(int orderId);
}