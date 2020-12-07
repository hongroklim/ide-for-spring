package dev.rokong.order.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.OrderDeliveryDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryService {
    
    @Autowired OrderDeliveryDAO oDeliveryDAO;

    @Autowired OrderService oService;

    public OrderDeliveryDTO getODelivery(int orderId){
        return oDeliveryDAO.selectOrderDelivery(orderId);
    }

    public OrderDeliveryDTO getODeliveryNotNull(int orderId){
        OrderDeliveryDTO oDelivery = this.getODelivery(orderId);
        if(oDelivery == null){
            throw new BusinessException(orderId + " : order delivery is not exists");
        }
        return oDelivery;
    }

    private OrderDeliveryDTO getODeliveryNotNull(OrderDeliveryDTO oDelivery){
        return this.getODeliveryNotNull(oDelivery.getOrderId());
    }
    
    public OrderDeliveryDTO createODelivery(OrderDeliveryDTO oDelivery){
        //avoid primary constraint
        if(this.getODelivery(oDelivery.getOrderId()) != null){
            log.debug("order delivery parameter : "+oDelivery);
            throw new BusinessException("order delivery already exists");
        }

        //verify parameter
        this.verifyParameter(oDelivery);

        //if sender name is not defined, set user name
        if(ObjUtil.isEmpty(oDelivery.getSenderNm())){
            OrderDTO order = oService.getOrderNotNull(oDelivery.getOrderId());
            oDelivery.setSenderNm(order.getUserNm());
        }

        //insert
        oDeliveryDAO.insertOrderDelivery(oDelivery);

        return this.getODeliveryNotNull(oDelivery);
    }
    
    public OrderDeliveryDTO updateODelivery(OrderDeliveryDTO oDelivery){
        this.getODeliveryNotNull(oDelivery);

        this.verifyParameter(oDelivery);

        //update
        oDeliveryDAO.updateOrderDelivery(oDelivery);

        return this.getODeliveryNotNull(oDelivery);
    }
    
    public void deleteODelivery(int orderId){
        this.getODeliveryNotNull(orderId);

        oDeliveryDAO.deleteOrderDelivery(orderId);
    }

    private void verifyParameter(OrderDeliveryDTO oDelivery){
        //order id exists in ord_main
        oService.getOrderNotNull(oDelivery.getOrderId());

        //recipient nm
        if(ObjUtil.isEmpty(oDelivery.getRecipientNm())){
            log.debug("order delivery parameter : "+oDelivery);
            throw new BusinessException("recipient name is empty");
        }

        //zip cd
        if(ObjUtil.isEmpty(oDelivery.getZipCd())){
            log.debug("order delivery parameter : "+oDelivery);
            throw new BusinessException("zip code is empty");
        }

        //address1
        if(ObjUtil.isEmpty(oDelivery.getAddress1())){
            log.debug("order delivery parameter : "+oDelivery);
            throw new BusinessException("recipient name is empty");
        }

        //contact1
        if(ObjUtil.isEmpty(oDelivery.getContact1())){
            log.debug("order delivery parameter : "+oDelivery);
            throw new BusinessException("recipient name is empty");
        }
    }
}