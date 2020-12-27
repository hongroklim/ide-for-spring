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
            = oDeliveryService.addODelivery(orderId, product.getDeliveryId());

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

        //set isValid
        oProduct.setIsValid(true);

        //insert
        oProductDAO.insert(oProduct);

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
        oProductDAO.updateCnt(oProduct);
        
        //update order main price
        this.updateOrderPrice(oProduct.getOrderId());
        
        return this.getOProductNotNull(oProduct);
    }
    
    public void deleteOProduct(OrderProductDTO oProduct){
        oProduct = this.getOProductNotNull(oProduct);
        
        oProductDAO.delete(oProduct);
        //update order main price
        this.updateOrderPrice(oProduct.getOrderId());
        
        boolean isDeliveryChanged
            = oDeliveryService.removeODelivery(oProduct.getOrderId(), oProduct.getDeliveryId());
        if(isDeliveryChanged){
            //update order main delivery price
            this.updateOrderDeliveryPrice(oProduct.getOrderId());
        }
    }

    public void updateOProductToNull(int productId, String optionCd){        
        OrderProductDTO oProduct = new OrderProductDTO();
        oProduct.setProductId(productId);
        oProduct.setOptionCd(optionCd);

        oProductDAO.updateToNull(oProduct);
    }

    private void updateOrderPrice(int orderId){
        List<OrderProductDTO> oProductList
            = oProductDAO.selectList(new OrderProductDTO(orderId));
        
        int totalPrice = 0;
        int itemPrice = 0;

        //sum product's price
        for(OrderProductDTO oProduct : oProductList){
            if(oProduct.getOrderStatus().isProcess() && oProduct.getIsValid()){
                itemPrice = oProduct.getPrice() + oProduct.getDiscountPrice();
                totalPrice += oProduct.getCnt() * itemPrice;
            }
        }

        orderService.updateOrderPrice(orderId, totalPrice);
    }

    private void updateOrderDeliveryPrice(int orderId){
        //get total delivery price
        int totalPrice = oDeliveryService.totalDeliveryPrice(orderId);
        
        //update
        orderService.updateOrderDeliveryPrice(orderId, totalPrice);
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

    /**
     * from to be order status, guess who change order status
     * between customer or seller
     *
     * @param status to be order status
     * @return if true, customer did
     */
    private boolean isDoneByCustomer(OrderStatus status) {
        if (status.isProcess()) {
            //to be normal process
            if (status == OrderStatus.CHECKING || status.isFormerThan(OrderStatus.CHECKING)) {
                //former or equal than checking, customer did
                return true;
            } else {
                //else, seller did
                return false;
            }
        } else {
            //to be canceled
            if (status.isSellerCancel()) {
                return false;
            } else {
                //customer includes CANCEL status
                return true;
            }
        }
    }

    public void updateOProductStatus(OrderProductDTO oProduct){
        //parameter : orderId, productId, optionCd, statusCd

        //get asis one (also verify parameter)
        OrderProductDTO asisOProduct = this.getOProductNotNull(oProduct);

        //verify tobe order status
        OrderStatus orderStatus = oProduct.getOrderStatus();
        if(orderStatus == null){
            throw new BusinessException("order status is not defined");
        }

        //set is valid
        boolean isValid = oProduct.getOrderStatus().isProcess();
        oProduct.setIsValid(isValid);

        //update order product
        oProductDAO.updateValidAndStatus(oProduct);

        //figure out editor name (customer or seller)
        String editorName = this.isDoneByCustomer(orderStatus) ? "" : asisOProduct.getSellerNm();

        //update order (or not if unnecessary)
        orderService.updateOrderStatus(oProduct.getOrderId(), editorName);

        //status change from normal process to cancel
        if(asisOProduct.getOrderStatus().isProcess()
                && oProduct.getOrderStatus().isCanceled()){
            //update order price
            this.updateOrderPrice(oProduct.getOrderId());

            boolean isDeliveryChanged
                    = oDeliveryService.removeODelivery(asisOProduct.getOrderId(), asisOProduct.getDeliveryId());
            if(isDeliveryChanged){
                //update order main delivery price
                this.updateOrderDeliveryPrice(oProduct.getOrderId());
            }
        }
    }

    public void updateStatusByOrder(int orderId, OrderStatus orderStatus){
        //referred by main order
        //update order status in order product (which is valid)

        //check order exists
        orderService.getOrderNotNull(orderId);

        //get order product list in order
        OrderProductDTO oProduct = new OrderProductDTO(orderId);
        List<OrderProductDTO> oProdList = this.getOProducts(oProduct);

        //create order product parameter and update
        boolean isValid = orderStatus.isProcess();
        oProduct.setIsValid(isValid);
        oProduct.setOrderStatus(orderStatus);

        //update valid and order status
        for(OrderProductDTO p : oProdList.stream()
                .filter(OrderProductDTO::getIsValid)    //valid order products
                .collect(Collectors.toList())){
            //set parameter and update
            oProduct.setProductId(p.getProductId());
            oProduct.setOptionCd(p.getOptionCd());
            oProductDAO.updateValidAndStatus(oProduct);
        }

        //if tobe order status is canceled
        if(!isValid){
            //update order product's price
            this.updateOrderPrice(orderId);

            //remove order product delivery for products in order
            boolean isDeliveryChanged = false;

            for(OrderProductDTO p : oProdList){
                boolean isChanged
                        = oDeliveryService.removeODelivery(p.getOrderId(), p.getDeliveryId());
                //set is delivery changed
                if (!isDeliveryChanged && isChanged) {
                    isDeliveryChanged = true;
                }
            }

            //update order main delivery price after loop
            if(isDeliveryChanged){
                this.updateOrderDeliveryPrice(orderId);
            }
        }
    }

    public OrderStatus getProperOrderStatus(int orderId) {
        //calculate the proper product status

        //get order products
        OrderProductDTO param = new OrderProductDTO(orderId);
        List<OrderProductDTO> oProducts = this.getOProducts(param);

        //if no product, return writing
        if (ObjUtil.isEmpty(oProducts)) {
            return OrderStatus.WRITING;
        }

        //get last process
        List<OrderStatus> orderStatuses
                = oProducts.stream()
                .map(OrderProductDTO::getOrderStatus)
                .collect(Collectors.toList());
        OrderStatus lastProcess = null;
        for (OrderStatus status : orderStatuses) {
            if (status.isProcess()) {
                //set last process when first or get former one
                if (lastProcess == null || status.isFormerThan(lastProcess)) {
                    lastProcess = status;
                }
            }
        }

        //if last process is null, all products are canceled
        return (lastProcess != null) ? lastProcess : OrderStatus.CANCEL;
    }

}
