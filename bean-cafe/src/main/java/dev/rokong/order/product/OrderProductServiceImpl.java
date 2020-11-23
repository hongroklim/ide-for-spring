package dev.rokong.order.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.OrderProductDTO;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.product.detail.ProductDetailService;
import dev.rokong.product.main.ProductService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderProductServiceImpl implements OrderProductService {
    
    @Autowired OrderProductDAO oProductDAO;

    @Autowired OrderService orderService;
    @Autowired ProductService pService;
    @Autowired ProductDetailService pDetailService;

    public List<OrderProductDTO> getOProducts(OrderProductDTO oProduct){
        return oProductDAO.selectOProductList(oProduct);
    }

    public OrderProductDTO getOProduct(OrderProductDTO oProduct){
        this.verifyPrimaryParameter(oProduct);
        return oProductDAO.selectOProduct(oProduct);
    }

    public OrderProductDTO getOProductNotNull(OrderProductDTO oProduct){
        OrderProductDTO result = this.getOProduct(oProduct);
        if(result == null){
            log.debug("order product parameter : "+oProduct.toString());
            throw new BusinessException("order product is not exists");
        }
        return result;
    }
    
    public OrderProductDTO addOProduct(OrderProductDTO oProduct){
        this.verifyPrimaryParameter(oProduct);

        //is cnt defined
        if(ObjUtil.isNotDefined(oProduct.getCnt())){
            log.debug("order product parameter : "+oProduct.toString());
            throw new BusinessException("cnt is not exists");
        }

        //is order exists
        orderService.getOrderNotNull(oProduct.getOrderId());

        //is product exists
        ProductDTO product = pService.getProductNotNull(oProduct.getProductId());

        //set product name
        oProduct.setProductNm(product.getName());

        //set price
        oProduct.setPrice(product.getPrice());
        oProduct.setDiscountPrice(product.getDiscountPrice());

        if(ObjUtil.isNotEmpty(oProduct.getOptionCd())){
            //is product detail exists
            ProductDetailDTO pDetail = new ProductDetailDTO(oProduct.getProductId(), oProduct.getOptionCd());
            pDetail = pDetailService.getDetailNotNull(pDetail);
            //set option name
            oProduct.setOptionNm(pDetail.getFullNm());

            //change price
            oProduct.setPrice(oProduct.getPrice()+pDetail.getPriceChange());
        }

        //insert
        oProductDAO.insertOProduct(oProduct);

        return this.getOProductNotNull(oProduct);
    }
    
    public OrderProductDTO updateOProduct(OrderProductDTO oProduct){
        this.getOProductNotNull(oProduct);
        //TODO
        return this.getOProductNotNull(oProduct);
    }
    
    public void deleteOProduct(OrderProductDTO oProduct){
        this.getOProductNotNull(oProduct);
        //TODO
    }

    private void verifyPrimaryParameter(OrderProductDTO oProduct){
        if(ObjUtil.isNotDefined(oProduct.getOrderId())){
            log.debug("order product parameter : "+oProduct.toString());
            throw new BusinessException("order id is not defined");
            
        }else if(ObjUtil.isNotDefined(oProduct.getProductId())){
            log.debug("order product parameter : "+oProduct.toString());
            throw new BusinessException("product id is not defined");
        }
    }
}
