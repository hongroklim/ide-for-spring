package com.company.product.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.annotation.DeliveryType;
import com.company.dto.ProductDeliveryDTO;
import com.company.exception.BusinessException;
import com.company.product.main.ProductService;
import com.company.user.UserService;
import com.company.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductDeliveryServiceImpl implements ProductDeliveryService {
    
    @Autowired ProductDeliveryDAO pDeliveryDAO;

    @Autowired UserService uService;
    @Autowired ProductService pService;

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

    public ProductDeliveryDTO getPDeliveryByProduct(int productId){
        return pDeliveryDAO.selectPDeliveryByProductId(productId);
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
        //verify must be defined parameter
        if(ObjUtil.isEmpty(pDelivery.getSellerNm())){
            log.debug("product delivery paramter :"+pDelivery.toString());
            throw new BusinessException("seller name is not defined in product delivery");
        }
        
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
        //if product exists, throw exception
        if(pService.getProductsByDelivery(id) != null){
            throw new BusinessException(id+" delivery id has product(s)");
        }

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
        if(ObjUtil.isNotEmpty(pDelivery.getSellerNm())){
            uService.getUserNotNull(pDelivery.getSellerNm());
        }
        
    }
}