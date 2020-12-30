package dev.rokong.order.delivery;

import java.util.List;
import java.util.stream.Collectors;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDeliveryDTO;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.util.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.product.delivery.ProductDeliveryService;
import dev.rokong.product.main.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryService {

    @Autowired
    private OrderDeliveryDAO oDeliveryDAO;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductService oProductService;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDeliveryService pDeliveryService;

    private void verifyPrimaryDefined(OrderDeliveryDTO oDelivery){
        //parameter object
        if(oDelivery == null){
            throw new BusinessException("order product delivery is null");
        }

        //order id
        if(oDelivery.getOrderId() == 0){
            log.debug("oDelivery parameter :"+oDelivery.toString());
            throw new BusinessException("order id is not defined in oDelivery");
        }

        //delivery id
        if(oDelivery.getDeliveryId() == 0){
            log.debug("oDelivery parameter :"+oDelivery.toString());
            throw new BusinessException("delivery id is not defined in oDelivery");
        }
    }

    public OrderDeliveryDTO getODelivery(OrderDeliveryDTO oDelivery){
        this.verifyPrimaryDefined(oDelivery);
        return oDeliveryDAO.select(oDelivery);
    }

    public OrderDeliveryDTO getODeliveryNotNull(OrderDeliveryDTO oDelivery){
        OrderDeliveryDTO getObj = this.getODelivery(oDelivery);
        if(getObj == null){
            log.debug("oDelivery parameter :"+oDelivery.toString());
            throw new BusinessException("oDelivery is not exists");
        }
        return getObj;
    }

    public void checkODeliveryExist(OrderDeliveryDTO oDelivery){
        this.verifyPrimaryDefined(oDelivery);

        if(oDeliveryDAO.count(oDelivery) == 0){
            log.debug("oDelivery parameter :"+oDelivery.toString());
            throw new BusinessException("oDelivery is not exists");
        }
    }

    public OrderDeliveryDTO createODelivery(OrderDeliveryDTO oDelivery){
        //verify all values are defined
        this.verifyPrimaryDefined(oDelivery);

        //avoid duplicate in order product delivery
        if(this.getODelivery(oDelivery) != null){
            log.debug("oDelivery parameter :"+oDelivery.toString());
            throw new BusinessException("oDelivery is already exists");
        }

        //is order exists
        orderService.checkOrderExist(oDelivery.getOrderId());

        //is product delivery exists
        ProductDeliveryDTO pDelivery
            = pDeliveryService.getPDeliveryNotNull(oDelivery.getDeliveryId());

        //set values by product delivery
        oDelivery.setSellerNm(pDelivery.getSellerNm());
        oDelivery.setTypeNm(pDelivery.getType());
        oDelivery.setDeliveryNm(pDelivery.getName());
        oDelivery.setDeliveryPrice(pDelivery.getPrice());

        //set default value
        oDelivery.setPrice(0);
        oDelivery.setOrderStatus(OrderStatus.WRITING);

        //insert
        oDeliveryDAO.insert(oDelivery);

        return this.getODeliveryNotNull(oDelivery);
    }

    public void addODelivery(int orderId, int deliveryId){
        //search oDelivery whether it is already exists
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        oDelivery = this.getODelivery(oDelivery);

        //if order delivery not exists, it will be created
        boolean tobeCreated = (oDelivery == null || oDelivery.getOrderStatus().isCanceled());

        if(tobeCreated){
            OrderDeliveryDTO param = new OrderDeliveryDTO(orderId, deliveryId);

            if(oDelivery == null){
                //if not exists, create oDelivery
                this.createODelivery(param);

            }else if(oDelivery.getOrderStatus().isCanceled()){
                //if existing one is canceled, update status
                OrderStatus orderStatus = oProductService.getProperOrderStatus(orderId, deliveryId);
                param.setOrderStatus(orderStatus);
                oDeliveryDAO.update(param);
            }

            //update delivery price in order
            orderService.updateOrderDeliveryPrice(orderId);
        }

        //update price
        this.updatePrice(orderId, deliveryId);
    }
    
    public void removeODelivery(int orderId, int deliveryId){
        //verify all values are defined
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        this.verifyPrimaryDefined(oDelivery);

        //is oDelivery exists
        this.checkODeliveryExist(oDelivery);

        int oProductCnt = oProductService.countOProductsByDelivery(orderId, deliveryId);
        if(oProductCnt > 0){
            //order products are exists
            log.debug("order products are exists : {}", oProductCnt);

            //update price
            this.updatePrice(orderId, deliveryId);

        }else{
            //order products are not exists, then cancel
            oDelivery.setOrderStatus(OrderStatus.CANCEL);
            oDeliveryDAO.update(oDelivery);

            //update delivery price in order
            orderService.updateOrderDeliveryPrice(orderId);

        }
    }

    private void updatePrice(int orderId, int deliveryId){
        //check order delivery exists
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        this.checkODeliveryExist(oDelivery);

        //get total price in order product
        int price = oProductService.totalPrice(orderId, deliveryId);
        oDelivery.setPrice(price);

        //update only price
        oDeliveryDAO.update(oDelivery);

        //update price in order
        orderService.updateOrderPrice(orderId);
    }

    public int totalDeliveryPrice(int orderId){
        List<OrderDeliveryDTO> list = oDeliveryDAO.selectByOrder(orderId);

        int deliveryPrice = 0;

        for(OrderDeliveryDTO item : list){
            if(item.getOrderStatus().isProcess()){
                deliveryPrice += item.getDeliveryPrice();
            }
        }

        return deliveryPrice;
    }

    public int totalPrice(int orderId){
        List<OrderDeliveryDTO> list = oDeliveryDAO.selectByOrder(orderId);

        int totalPrice = 0;
        for(OrderDeliveryDTO item : list){
            if(item.getOrderStatus().isProcess()){
                totalPrice += item.getPrice();
            }
        }

        return totalPrice;
    }

    public OrderDeliveryDTO updateShipCd(OrderDeliveryDTO oDelivery){
        this.checkODeliveryExist(oDelivery);

        //update only ship cd
        oDelivery.setPrice(null);
        oDelivery.setOrderStatus(null);
        oDeliveryDAO.update(oDelivery);

        return this.getODeliveryNotNull(oDelivery);
    }

    public void updateStatusByOrder(int orderId, OrderStatus orderStatus){

        //verify parameters
        if (orderId == 0) {
            throw new BusinessException("order id is not defined");
        } else if (orderStatus == null) {
            throw new BusinessException("order status is not defined");
        }

        //check order exists
        orderService.checkOrderExist(orderId);

        //get order delivery list in order
        List<OrderDeliveryDTO> oDlvrList = oDeliveryDAO.selectByOrder(orderId);

        //prepare parameter
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId);
        oDelivery.setOrderStatus(orderStatus);
        oDelivery.setPrice(null);
        oDelivery.setShipCd(null);

        //update order delivery (valid)
        for(OrderDeliveryDTO d : oDlvrList.stream()
                .filter(p -> p.getOrderStatus().isProcess())    //valid order delivery
                .collect(Collectors.toList())){
            //set parameter and update
            oDelivery.setDeliveryId(d.getDeliveryId());
            oDeliveryDAO.update(oDelivery);

            //update products by order and delivery id
            oProductService.updateStatusByDelivery(d.getOrderId(), d.getDeliveryId(), orderStatus);
        }
    }

    public void updateStatus(int orderId, int deliveryId){
        //create parameter and get order delivery (with verifying)
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        oDelivery = this.getODelivery(oDelivery);

        //if order delivery is not exists, return
        if(oDelivery == null){
            return;
        }

        //get proper status in order products
        OrderStatus tobeStatus = oProductService.getProperOrderStatus(orderId, deliveryId);

        //if tobe order product is different, update
        if(oDelivery.getOrderStatus() != tobeStatus){
            oDelivery.setOrderStatus(tobeStatus);
            oDelivery.setPrice(null);
            oDelivery.setShipCd(null);
            oDeliveryDAO.update(oDelivery);
        }

        //also update order's status
        orderService.updateOrderStatus(orderId);
    }

    public OrderStatus getProperOrderStatus(int orderId){
        //calculate the proper product status

        //get order deliveries
        List<OrderDeliveryDTO> list = oDeliveryDAO.selectByOrder(orderId);

        return orderService.getProperOrderStatus(list);
    }
}