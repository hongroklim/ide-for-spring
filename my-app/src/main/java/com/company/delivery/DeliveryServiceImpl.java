package dev.rokong.delivery;

import dev.rokong.dto.DeliveryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.OrderDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService {
    
    @Autowired
    DeliveryDAO deliveryDAO;

    @Autowired OrderService oService;

    public DeliveryDTO getDelivery(int orderId){
        return deliveryDAO.select(orderId);
    }

    public DeliveryDTO getDeliveryNotNull(int orderId){
        DeliveryDTO delivery = this.getDelivery(orderId);
        if(delivery == null){
            throw new BusinessException(orderId + " : order delivery is not exists");
        }
        return delivery;
    }

    public void checkDeliveryExist(int orderId){
        if(orderId == 0){
            throw new IllegalArgumentException("order id is not defined");
        }

        if (deliveryDAO.count(orderId) == 0) {
            throw new BusinessException(orderId + " delivery is not exists");
        }
    }

    private DeliveryDTO getDeliveryNotNull(DeliveryDTO delivery){
        return this.getDeliveryNotNull(delivery.getOrderId());
    }
    
    public DeliveryDTO createDelivery(DeliveryDTO delivery){
        //avoid primary constraint
        if(this.getDelivery(delivery.getOrderId()) != null){
            log.debug("order delivery parameter : "+delivery);
            throw new BusinessException("order delivery already exists");
        }

        //verify parameter
        this.verifyParameter(delivery);

        //if sender name is not defined, set user name
        if(ObjUtil.isEmpty(delivery.getSenderNm())){
            OrderDTO order = oService.getOrderNotNull(delivery.getOrderId());
            delivery.setSenderNm(order.getUserNm());
        }

        //insert
        deliveryDAO.insert(delivery);

        return this.getDeliveryNotNull(delivery);
    }
    
    public DeliveryDTO updateDelivery(DeliveryDTO delivery){
        this.getDeliveryNotNull(delivery);

        this.verifyParameter(delivery);

        //update
        deliveryDAO.update(delivery);

        return this.getDeliveryNotNull(delivery);
    }
    
    public void deleteDelivery(int orderId){
        this.checkDeliveryExist(orderId);

        deliveryDAO.delete(orderId);
    }

    private void verifyParameter(DeliveryDTO delivery){
        //order id exists in ord_main
        oService.checkOrderExist(delivery.getOrderId());

        //recipient nm
        if(ObjUtil.isEmpty(delivery.getRecipientNm())){
            log.debug("order delivery parameter : "+delivery);
            throw new BusinessException("recipient name is empty");
        }

        //zip cd
        if(ObjUtil.isEmpty(delivery.getZipCd())){
            log.debug("order delivery parameter : "+delivery);
            throw new BusinessException("zip code is empty");
        }

        //address1
        if(ObjUtil.isEmpty(delivery.getAddress1())){
            log.debug("order delivery parameter : "+delivery);
            throw new BusinessException("recipient name is empty");
        }

        //contact1
        if(ObjUtil.isEmpty(delivery.getContact1())){
            log.debug("order delivery parameter : "+delivery);
            throw new BusinessException("recipient name is empty");
        }
    }
}