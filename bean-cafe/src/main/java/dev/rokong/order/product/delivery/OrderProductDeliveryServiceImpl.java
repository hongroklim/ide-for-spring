package dev.rokong.order.product.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.OrderProductDeliveryDTO;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.product.delivery.ProductDeliveryService;
import dev.rokong.product.main.ProductService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderProductDeliveryServiceImpl implements OrderProductDeliveryService {
    
    @Autowired OrderProductDeliveryDAO oPDeliveryDAO;

    @Autowired OrderService oService;

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

        //seller name
        if(ObjUtil.isEmpty(oPDelivery.getSellerNm())){
            log.debug("oPDelivery parameter :"+oPDelivery.toString());
            throw new BusinessException("seller name is not defined in oPDelivery");
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
        //if seller name is not defined
        ProductDeliveryDTO pDelivery = null;    //to reuse after verify
        if(ObjUtil.isEmpty(oPDelivery.getSellerNm())){
            if(oPDelivery.getDeliveryId() != 0){
                //set seller name
                pDelivery = pDeliveryService.getPDeliveryNotNull(oPDelivery.getDeliveryId());
                oPDelivery.setSellerNm(pDelivery.getSellerNm());
            }
        }

        //verify all values are defined
        this.verifyPrimaryDefined(oPDelivery);

        //avoid duplicate
        if(this.getOPDelivery(oPDelivery) != null){
            log.debug("oPDelivery parameter :"+oPDelivery.toString());
            throw new BusinessException("oPDelivery is already exists");
        }

        //is order exists
        oService.getOrderNotNull(oPDelivery.getOrderId());

        //is produt delivery exists
        if(pDelivery == null){
            pDeliveryService.getPDeliveryNotNull(oPDelivery.getDeliveryId());
        }

        //set values by product delivery
        oPDelivery.setTypeNm(pDelivery.getType());
        oPDelivery.setDeliveryNm(pDelivery.getName());
        oPDelivery.setPrice(pDelivery.getPrice());

        //insert
        oPDeliveryDAO.insertOPDelivery(oPDelivery);

        return this.getOPDeliveryNotNull(oPDelivery);
    }

    public OrderProductDeliveryDTO addOPDelivery(int orderId, int productId){
        //is order exists
        oService.getOrderNotNull(orderId);

        //is product exists
        pService.getProductNotNull(productId);

        //get productDelivery by productId
        ProductDeliveryDTO pDelivery = pDeliveryService.getPDeliveryByProduct(productId);

        //search oPDelivery whether it is already exists
        OrderProductDeliveryDTO oPDelivery = this.orderAndPDelivery(orderId, pDelivery);
        oPDelivery = this.getOPDelivery(oPDelivery);

        if(oPDelivery != null){
            //oPDelivery is already exists
            return oPDelivery;
        }else{
            //if not exists, create oPDelivery
            return this.createOPDelivery(oPDelivery);
        }
    }
    
    public boolean removeOPDelivery(int orderId, int deliveryId){
        //TODO removeOPDelivery
        return false;
    }

    private OrderProductDeliveryDTO orderAndPDelivery(int orderId, ProductDeliveryDTO pDelivery){
        if(orderId == 0){
            throw new BusinessException("order id is not defined");
        }

        if(pDelivery == null){
            throw new BusinessException("product delivery is not defined");
        }else{
            //check product delivery's values are defined
            if(pDelivery.getId() == 0){
                throw new BusinessException("product delivery id is not defined");
            }else if(ObjUtil.isEmpty(pDelivery.getSellerNm())){
                throw new BusinessException("seller name is not defined");
            }
        }

        return new OrderProductDeliveryDTO(orderId, pDelivery.getSellerNm(), pDelivery.getId());
    }
}