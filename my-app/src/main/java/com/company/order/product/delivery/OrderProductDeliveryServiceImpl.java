package com.company.order.product.delivery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.dto.OrderProductDeliveryDTO;
import com.company.dto.ProductDeliveryDTO;
import com.company.exception.BusinessException;
import com.company.order.main.OrderService;
import com.company.order.product.OrderProductService;
import com.company.product.delivery.ProductDeliveryService;
import com.company.product.main.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderProductDeliveryServiceImpl implements OrderProductDeliveryService {
    
    @Autowired OrderProductDeliveryDAO oPDeliveryDAO;

    @Autowired OrderService oService;
    @Autowired OrderProductService oProductService;

    @Autowired ProductService pService;
    @Autowired ProductDeliveryService pDeliveryService;

    private void verifyPrimaryDefined(OrderProductDeliveryDTO oPDelivery){
        //parameter object
        if(oPDelivery == null){
            throw new BusinessException("order product delivery is null");
        }

        //order id
        if(oPDelivery.getOrderId() == 0){
            log.debug("oPDelivery parameter :"+oPDelivery.toString());
            throw new BusinessException("order id is not defined in oPDelivery");
        }

        //delivery id
        if(oPDelivery.getDeliveryId() == 0){
            log.debug("oPDelivery parameter :"+oPDelivery.toString());
            throw new BusinessException("delivery id is not defined in oPDelivery");
        }
    }

    public OrderProductDeliveryDTO getOPDelivery(OrderProductDeliveryDTO oPDelivery){
        this.verifyPrimaryDefined(oPDelivery);
        return oPDeliveryDAO.selectOPDelivery(oPDelivery);
    }

    public OrderProductDeliveryDTO getOPDeliveryNotNull(OrderProductDeliveryDTO oPDelivery){
        OrderProductDeliveryDTO getObj = this.getOPDelivery(oPDelivery);
        if(getObj == null){
            log.debug("oPDelivery parameter :"+oPDelivery.toString());
            throw new BusinessException("oPDelivery is not exists");
        }
        return getObj;
    }

    public OrderProductDeliveryDTO createOPDelivery(OrderProductDeliveryDTO oPDelivery){
        //verify all values are defined
        this.verifyPrimaryDefined(oPDelivery);

        //avoid duplicate in order product delivery
        if(this.getOPDelivery(oPDelivery) != null){
            log.debug("oPDelivery parameter :"+oPDelivery.toString());
            throw new BusinessException("oPDelivery is already exists");
        }

        //is order exists
        oService.getOrderNotNull(oPDelivery.getOrderId());

        //is produt delivery exists
        ProductDeliveryDTO pDelivery
            = pDeliveryService.getPDeliveryNotNull(oPDelivery.getDeliveryId());

        //set values by product delivery
        oPDelivery.setSellerNm(pDelivery.getSellerNm());
        oPDelivery.setTypeNm(pDelivery.getType());
        oPDelivery.setDeliveryNm(pDelivery.getName());
        oPDelivery.setPrice(pDelivery.getPrice());

        //insert
        oPDeliveryDAO.insertOPDelivery(oPDelivery);

        return this.getOPDeliveryNotNull(oPDelivery);
    }

    public boolean addOPDelivery(int orderId, int deliveryId){
        //search oPDelivery whether it is already exists
        OrderProductDeliveryDTO oPDelivery = new OrderProductDeliveryDTO(orderId, deliveryId);
        oPDelivery = this.getOPDelivery(oPDelivery);

        if(oPDelivery != null){
            //oPDelivery is already exists
            return false;
        }else{
            //if not exists, create oPDelivery
            oPDelivery = new OrderProductDeliveryDTO(orderId, deliveryId);
            this.createOPDelivery(oPDelivery);
            return true;
        }
    }
    
    public boolean removeOPDelivery(int orderId, int deliveryId){
        //verfiy all values are defined
        OrderProductDeliveryDTO oPDelivery = new OrderProductDeliveryDTO(orderId, deliveryId);
        this.verifyPrimaryDefined(oPDelivery);

        //is oPDelivery exists
        this.getOPDeliveryNotNull(oPDelivery);
        
        int oProductCnt = oProductService.countOProductsByDelivery(orderId, deliveryId);
        if(oProductCnt > 0){
            //order products are exists
            log.debug("order products are exists :"+oProductCnt);
            return false;
        }else{
            //order products are not exists, then delete
            oPDeliveryDAO.deleteOPDelivery(oPDelivery);
            return true;
        }
    }

    public int totalDeliveryPrice(int orderId){
        List<OrderProductDeliveryDTO> list = oPDeliveryDAO.selectOPdeliveriesByOrder(orderId);

        int totalPrice = 0;

        for(OrderProductDeliveryDTO item : list){
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }
}