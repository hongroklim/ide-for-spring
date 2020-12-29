package dev.rokong.order.product;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.order.delivery.OrderDeliveryService;
import dev.rokong.product.detail.ProductDetailService;
import dev.rokong.product.main.ProductService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderProductServiceImpl implements OrderProductService {
    
    @Autowired
    private OrderProductDAO oProductDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDeliveryService oDeliveryService;

    @Autowired
    private ProductService pService;

    @Autowired
    private ProductDetailService pDetailService;

    public List<OrderProductDTO> getOProducts(OrderProductDTO oProduct){
        return oProductDAO.selectList(oProduct);
    }

    public OrderProductDTO getOProduct(OrderProductDTO oProduct){
        this.verifyPrimaryValuesDefined(oProduct);
        return oProductDAO.select(oProduct);
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
        oProduct.setDeliveryId(null);
        OrderProductDTO getOProduct = this.getOProduct(oProduct);
        if(getOProduct != null){
            log.debug("order product in table : "+getOProduct.toString());
            throw new BusinessException("order product already exists");
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
        oProductDAO.insert(oProduct);

        //after insert order product, update order delivery
        oDeliveryService.addODelivery(orderId, product.getDeliveryId());

        return this.getOProductNotNull(oProduct);
    }
    
    public OrderProductDTO updateOProductCnt(OrderProductDTO oProduct){
        //update cnt
        OrderProductDTO getOProd = this.getOProductNotNull(oProduct);
        
        //verify cnt
        this.verifyCnt(oProduct.getCnt());

        //update
        oProductDAO.updateCnt(oProduct);
        
        //update price in order delivery
        oDeliveryService.addODelivery(getOProd.getOrderId(), getOProd.getDeliveryId());
        
        return this.getOProductNotNull(oProduct);
    }
    
    public void deleteOProduct(OrderProductDTO oProduct){
        oProduct = this.getOProductNotNull(oProduct);
        
        oProductDAO.delete(oProduct);

        oDeliveryService.removeODelivery(oProduct.getOrderId(), oProduct.getDeliveryId());
    }

    public void updateOProductToNull(int productId, String optionCd){        
        OrderProductDTO oProduct = new OrderProductDTO();
        oProduct.setProductId(productId);
        oProduct.setOptionCd(optionCd);

        oProductDAO.updateToNull(oProduct);
    }

    private int totalPriceInList(List<? extends OrderProductDTO> list){
        int totalPrice = 0;
        int itemPrice = 0;

        //sum product's price
        for(OrderProductDTO oProduct : list){
            if(oProduct.getOrderStatus().isProcess()){
                itemPrice = oProduct.getPrice() + oProduct.getDiscountPrice();
                totalPrice += oProduct.getCnt() * itemPrice;
            }
        }

        return totalPrice;
    }

    public int totalPrice(int orderId){
        List<OrderProductDTO> oProductList
                = oProductDAO.selectList(new OrderProductDTO(orderId));

        return this.totalPriceInList(oProductList);
    }
    
    private void verifyPrimaryValuesDefined(OrderProductDTO oProduct){
        if(oProduct.getOrderId() == 0){
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
        if(orderId == 0){
            throw new BusinessException("order id is not defined");

        }else if(deliveryId == 0){
            throw new BusinessException("delivery id is not defined");
        }

        //set parameter
        OrderProductDTO oProduct = new OrderProductDTO(orderId);
        oProduct.setDeliveryId(deliveryId);

        return oProductDAO.countByDelivery(oProduct);
    }

    public int totalPrice(int orderId, int deliveryId){
        //create parameter
        OrderProductDTO param = new OrderProductDTO();
        param.setOrderId(orderId);
        param.setDeliveryId(deliveryId);

        //get list
        List<OrderProductDTO> list = oProductDAO.selectList(param);

        return this.totalPriceInList(list);
    }

    public void updateStatus(OrderProductDTO oProduct){
        //parameter : orderId, productId, optionCd, statusCd

        if(oProduct == null){
            throw new IllegalArgumentException("order product is null");
        }

        //verify tobe order status
        OrderStatus orderStatus = oProduct.getOrderStatus();
        if(orderStatus == null){
            throw new BusinessException("order status is not defined");
        }

        //get asis one (also verify parameter)
        OrderProductDTO asisOProduct = this.getOProductNotNull(oProduct);

        if (asisOProduct.getOrderStatus().equals(orderStatus)) {
            log.debug("asis and tobe order status are equal. not to be updated");
            return;
        }

        //update order product
        oProductDAO.updateStatus(oProduct);

        //status change from normal process to cancel
        if(asisOProduct.getOrderStatus().isProcess()
                && oProduct.getOrderStatus().isCanceled()){
            //remove order delivery
            oDeliveryService.removeODelivery(asisOProduct.getOrderId(), asisOProduct.getDeliveryId());
        }

        //update status
        oDeliveryService.updateStatus(asisOProduct.getOrderId(), asisOProduct.getDeliveryId());
    }

    public void updateStatusByDelivery(int orderId, int deliveryId, OrderStatus orderStatus){
        //check order exists
        orderService.getOrderNotNull(orderId);

        //create param and get lists
        OrderProductDTO oProduct = new OrderProductDTO();
        oProduct.setOrderId(orderId);
        oProduct.setDeliveryId(deliveryId);
        List<OrderProductDTO> oProdList = this.getOProducts(oProduct);

        //create order product parameter and update
        oProduct.setOrderStatus(orderStatus);

        //update valid and order status
        for(OrderProductDTO p : oProdList.stream()
                .filter(p -> p.getOrderStatus().isProcess())    //valid order products
                .collect(Collectors.toList())){
            //set parameter and update
            oProduct.setProductId(p.getProductId());
            oProduct.setOptionCd(p.getOptionCd());
            oProductDAO.updateStatus(oProduct);
        }

        //if tobe order status is canceled
        if(orderStatus.isCanceled()){
            //update order product's price
            oDeliveryService.removeODelivery(oProduct.getOrderId(), oProduct.getDeliveryId());
        }
    }

    public OrderStatus getProperOrderStatus(int orderId) {
        //calculate the proper product status

        //get order products
        OrderProductDTO param = new OrderProductDTO(orderId);
        List<OrderProductDTO> oProducts = this.getOProducts(param);

        return orderService.getProperOrderStatus(oProducts);
    }

    public OrderStatus getProperOrderStatus(int orderId, int deliveryId){
        //calculate the proper product status

        //get order products
        OrderProductDTO param = new OrderProductDTO();
        param.setOrderId(orderId);
        param.setDeliveryId(deliveryId);
        List<OrderProductDTO> oProducts = this.getOProducts(param);

        return orderService.getProperOrderStatus(oProducts);
    }


}
