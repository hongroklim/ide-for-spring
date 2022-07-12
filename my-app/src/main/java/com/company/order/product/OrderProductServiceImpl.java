package com.company.order.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.annotation.OrderStatus;
import com.company.dto.OrderProductDTO;
import com.company.dto.ProductDTO;
import com.company.dto.ProductDetailDTO;
import com.company.exception.BusinessException;
import com.company.order.main.OrderService;
import com.company.order.product.delivery.OrderProductDeliveryService;
import com.company.product.detail.ProductDetailService;
import com.company.product.main.ProductService;
import com.company.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderProductServiceImpl implements OrderProductService {
    
    @Autowired OrderProductDAO oProductDAO;

    @Autowired OrderService orderService;
    @Autowired OrderProductDeliveryService oPDeliveryService;

    @Autowired ProductService pService;
    @Autowired ProductDetailService pDetailService;

    public List<OrderProductDTO> getOProducts(OrderProductDTO oProduct){
        return oProductDAO.selectOProductList(oProduct);
    }

    public OrderProductDTO getOProduct(OrderProductDTO oProduct){
        this.verifyPrimaryValuesDefined(oProduct);
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
        this.verifyPrimaryValuesDefined(oProduct);

        //is cnt defined
        this.verifyCnt(oProduct.getCnt());

        //is order product already exists
        OrderProductDTO getOProduct = this.getOProduct(oProduct);
        if(getOProduct != null){
            log.debug("order product in table : "+getOProduct.toString());
            throw new BusinessException("order porduct already exists");
        }

        int orderId = oProduct.getOrderId();
        int productId = oProduct.getProductId();

        //is order exists
        orderService.getOrderNotNull(orderId);

        //is product exists
        ProductDTO product = pService.getProductNotNull(productId);

        //set product and seller name
        oProduct.setProductNm(product.getName());
        oProduct.setSellerNm(product.getSellerNm());

        //set price
        oProduct.setPrice(product.getPrice());
        oProduct.setDiscountPrice(product.getDiscountPrice());

        //set delivery id
        oProduct.setDeliveryId(product.getDeliveryId());

        //add order product delivery
        boolean isDeliveryChanged
            = oPDeliveryService.addOPDelivery(orderId, product.getDeliveryId());

        if(ObjUtil.isEmpty(oProduct.getOptionCd())
                || OrderProductDTO.NULL_OPTION_CD.equals(oProduct.getOptionCd())){
            //is only product (not with option cd) exists
            oProduct.setOptionCd(OrderProductDTO.NULL_OPTION_CD);

        }else{
            //is product detail exists
            ProductDetailDTO pDetail = new ProductDetailDTO(
                    productId, oProduct.getOptionCd()
                );
            pDetail = pDetailService.getDetailNotNull(pDetail);
            
            //set option name
            oProduct.setOptionNm(pDetail.getFullNm());

            //change price
            oProduct.setPrice(oProduct.getPrice()+pDetail.getPriceChange());
        }

        //set status
        oProduct.setOrderStatus(OrderStatus.WRITING);

        //insert
        oProductDAO.insertOProduct(oProduct);

        //update order main price and delivery price
        this.updateOrderPrice(orderId);
        if(isDeliveryChanged){
            this.updateOrderDeliveryPrice(orderId);
        }

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
        oProduct = this.getOProductNotNull(oProduct);
        
        oProductDAO.deleteOProduct(oProduct);
        //update order main price
        this.updateOrderPrice(oProduct.getOrderId());
        
        boolean isDeliveryChanged
            = oPDeliveryService.removeOPDelivery(oProduct.getOrderId(), oProduct.getProductId());
        if(isDeliveryChanged){
            //update order main delivery price
            this.updateOrderDeliveryPrice(oProduct.getOrderId());
        }
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
            if(oProduct.getOrderStatus().isProcess()){
                itemPrice = oProduct.getPrice() + oProduct.getDiscountPrice();
                totalPrice += oProduct.getCnt() * itemPrice;
            }
        }

        orderService.updateOrderPrice(orderId, totalPrice);
    }

    private void updateOrderDeliveryPrice(int orderId){
        //get total delivery price
        int totalPrice = oPDeliveryService.totalDeliveryPrice(orderId);
        
        //update
        orderService.updateOrderDeliveryPrice(orderId, totalPrice);
    }
    
    private void verifyPrimaryValuesDefined(OrderProductDTO oProduct){
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

    public int countOProductsByDelivery(int orderId, int deliveryId){
        //verify parameter
        if(ObjUtil.isEmpty(orderId)){
            throw new BusinessException("order id is not defined");
            
        }else if(ObjUtil.isEmpty(deliveryId)){
            throw new BusinessException("delivery id is not defined");
        }

        //set parameter
        OrderProductDTO oProduct = new OrderProductDTO(orderId);
        oProduct.setDeliveryId(deliveryId);

        return oProductDAO.countOProductsByDelivery(oProduct);
    }
}
