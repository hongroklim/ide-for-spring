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
        this.verifyCnt(oProduct.getCnt());

        //is order exists
        orderService.getOrderNotNull(oProduct.getOrderId());

        //is order product already exists
        OrderProductDTO getOProduct = this.getOProduct(oProduct);
        if(getOProduct != null){
            log.debug("order product in table : "+getOProduct.toString());
            throw new BusinessException("order porduct already exists");
        }

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

        //update order main price
        this.updateOrderPrice(oProduct.getOrderId());

        return this.getOProductNotNull(oProduct);
    }
    
    public OrderProductDTO updateOProductCnt(OrderProductDTO oProduct){
        //update cnt
        this.getOProductNotNull(oProduct);
        
        //verify cnt
        this.verifyCnt(oProduct.getCnt());

        //update
        oProductDAO.updateOProductCnt(oProduct);
        
        //update order main price
        this.updateOrderPrice(oProduct.getOrderId());

        return this.getOProductNotNull(oProduct);
    }
    
    public void deleteOProduct(OrderProductDTO oProduct){
        this.getOProductNotNull(oProduct);
        
        oProductDAO.deleteOProduct(oProduct);

        //update order main price
        this.updateOrderPrice(oProduct.getOrderId());
    }

    public void updateOProductToNull(int productId, String optionCd){
        OrderProductDTO oProduct = new OrderProductDTO();
        oProduct.setProductId(productId);
        oProduct.setOptionCd(optionCd);

        oProductDAO.updateOProductToNull(oProduct);
    }

    private void updateOrderPrice(int orderId){
        List<OrderProductDTO> oProductList
            = oProductDAO.selectOProductList(new OrderProductDTO(orderId));
        
        int totalPrice = 0;
        int itemPrice = 0;

        //sum product's price
        for(OrderProductDTO oProduct : oProductList){
            if(oProduct.getIsValid()){
                itemPrice = oProduct.getPrice() + oProduct.getDiscountPrice();
                totalPrice += oProduct.getCnt() * itemPrice;
            }
        }

        orderService.updateOrderPrice(orderId, totalPrice);
    }
    
    private void verifyPrimaryParameter(OrderProductDTO oProduct){
        if(ObjUtil.isEmpty(oProduct.getOrderId())){
            log.debug("order product parameter : "+oProduct.toString());
            throw new BusinessException("order id is not defined");
            
        }else if(ObjUtil.isEmpty(oProduct.getProductId())){
            log.debug("order product parameter : "+oProduct.toString());
            throw new BusinessException("product id is not defined");
        }
    }

    private void verifyCnt(Integer cnt){
        if(ObjUtil.isEmpty(cnt) || cnt <= 0){
            log.debug("cnt : "+cnt);
            throw new BusinessException("count must be greater than 0");
        }
    }
}
