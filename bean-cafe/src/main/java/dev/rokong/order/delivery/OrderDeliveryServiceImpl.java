package dev.rokong.order.delivery;

import java.util.List;

import dev.rokong.dto.OrderDeliveryDTO;
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
    OrderDeliveryDAO oDeliveryDAO;

    @Autowired OrderService oService;
    @Autowired OrderProductService oProductService;

    @Autowired ProductService pService;
    @Autowired ProductDeliveryService pDeliveryService;

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

    public OrderDeliveryDTO createoDelivery(OrderDeliveryDTO oDelivery){
        //verify all values are defined
        this.verifyPrimaryDefined(oDelivery);

        //avoid duplicate in order product delivery
        if(this.getODelivery(oDelivery) != null){
            log.debug("oDelivery parameter :"+oDelivery.toString());
            throw new BusinessException("oDelivery is already exists");
        }

        //is order exists
        oService.getOrderNotNull(oDelivery.getOrderId());

        //is produt delivery exists
        ProductDeliveryDTO pDelivery
            = pDeliveryService.getPDeliveryNotNull(oDelivery.getDeliveryId());

        //set values by product delivery
        oDelivery.setSellerNm(pDelivery.getSellerNm());
        oDelivery.setTypeNm(pDelivery.getType());
        oDelivery.setDeliveryNm(pDelivery.getName());
        oDelivery.setPrice(pDelivery.getPrice());

        //insert
        oDeliveryDAO.insert(oDelivery);

        return this.getODeliveryNotNull(oDelivery);
    }

    public boolean addODelivery(int orderId, int deliveryId){
        //search oDelivery whether it is already exists
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        oDelivery = this.getODelivery(oDelivery);

        if(oDelivery != null){
            //oDelivery is already exists
            return false;
        }else{
            //if not exists, create oDelivery
            oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
            this.createoDelivery(oDelivery);
            return true;
        }
    }
    
    public boolean removeODelivery(int orderId, int deliveryId){
        //verfiy all values are defined
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        this.verifyPrimaryDefined(oDelivery);

        //is oDelivery exists
        this.getODeliveryNotNull(oDelivery);
        
        int oProductCnt = oProductService.countOProductsByDelivery(orderId, deliveryId);
        if(oProductCnt > 0){
            //order products are exists
            log.debug("order products are exists : {}", oProductCnt);
            return false;
        }else{
            //order products are not exists, then delete
            oDeliveryDAO.delete(oDelivery);
            return true;
        }
    }

    public int totalDeliveryPrice(int orderId){
        List<OrderDeliveryDTO> list = oDeliveryDAO.selectByOrder(orderId);

        int totalPrice = 0;

        for(OrderDeliveryDTO item : list){
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }
}