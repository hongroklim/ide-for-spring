package dev.rokong.product.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.annotation.DeliveryType;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.user.UserService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductDeliveryServiceImpl implements ProductDeliveryService {
    
    @Autowired ProductDeliveryDAO pDeliveryDAO;

    @Autowired UserService uService;

    public ProductDeliveryDTO getPDelivery(int id){
        return pDeliveryDAO.selectPDelivery(id);
    }

    public ProductDeliveryDTO getPDeliveryNotNull(int id){
        ProductDeliveryDTO pDelivery = this.getPDelivery(id);
        if(pDelivery == null){
            throw new BusinessException(id+" product delivery is not exists");
        }
        return pDelivery;
    }
    
    private ProductDeliveryDTO getPDeliveryNotNull(ProductDeliveryDTO pDelivery){
        return this.getPDeliveryNotNull(pDelivery.getId());
    }

    public ProductDeliveryDTO initDefaultPDelivery(String sellerNm, int price){
        ProductDeliveryDTO pDelivery = new ProductDeliveryDTO();

        //set default values
        pDelivery.setSellerNm(sellerNm);
        pDelivery.setDeliveryType(DeliveryType.ETC);
        pDelivery.setPrice(price);

        //create product delivery
        return this.createPDelivery(pDelivery);
    }

    public ProductDeliveryDTO createPDelivery(ProductDeliveryDTO pDelivery){
        this.verifyPDeliveryParameter(pDelivery);

        //insert
        pDeliveryDAO.insertPDelivery(pDelivery);

        return this.getPDeliveryNotNull(pDelivery);
    }

    public ProductDeliveryDTO updatePDelivery(ProductDeliveryDTO pDelivery){
        this.verifyPDeliveryParameter(pDelivery);

        //update
        pDeliveryDAO.updatePDelivery(pDelivery);

        return this.getPDeliveryNotNull(pDelivery);
    }

    public void deletePDelivery(int id){
        pDeliveryDAO.deletePDelivery(id);
    }

    private void verifyPDeliveryParameter(ProductDeliveryDTO pDelivery){
        //is price defined
        if(ObjUtil.isEmpty(pDelivery.getPrice())){
            log.debug("product delivery paramter :"+pDelivery.toString());
            throw new BusinessException("price is not defined in product delivery");
        }

        //is deliveryType defined
        if(pDelivery.getDeliveryType() == null){
            log.debug("product delivery paramter :"+pDelivery.toString());
            throw new BusinessException("delivery type is not defined in product delivery");
        }

        //is seller exists
        uService.getUserNotNull(pDelivery.getSellerNm());
    }
}