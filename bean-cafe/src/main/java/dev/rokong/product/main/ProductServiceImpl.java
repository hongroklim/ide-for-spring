package dev.rokong.product.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.category.CategoryService;
import dev.rokong.dto.CategoryDTO;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.product.delivery.ProductDeliveryService;
import dev.rokong.product.option.ProductOptionService;
import dev.rokong.user.UserService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private UserService uService;

    @Autowired
    private CategoryService cService;

    @Autowired
    private ProductOptionService pOptionService;

    @Autowired
    private ProductDeliveryService pDeliveryService;

    @Autowired
    private ProductDAO pDAO;

    public List<ProductDTO> getProductList(){
        return pDAO.selectList();
    }

    private void initDefaultParameter(ProductDTO product){
        //name must be defined
        if(ObjUtil.isEmpty(product.getName())){
            log.debug("product parameter :"+product.toString());
            throw new BusinessException("product name must be defined");
        }

        //price must be defined
        if(ObjUtil.isEmpty(product.getPrice())){
            log.debug("product parameter :"+product.toString());
            throw new BusinessException("product price must be defined");
        }

        if(ObjUtil.isEmpty(product.getCategoryId())){
            //set default category to etc
            product.setCategoryId(CategoryDTO.ETC_ID);
        }
        
        //set default enabled
        if(product.getEnabled() == null){
            product.setEnabled(false);
        }

        //seller name must be defined
        if(ObjUtil.isEmpty(product.getSellerNm())){
            log.debug("product parameter :"+product.toString());
            throw new BusinessException("product seller must be defined");
        }

        //set default stock count
        if(ObjUtil.isEmpty(product.getStockCnt())){
            product.setStockCnt(0);
        }

        //set default discount price
        if(ObjUtil.isEmpty(product.getDiscountPrice())){
            product.setDiscountPrice(0);
        }

        //if deliveryId is not defined, create delivery group
        ProductDeliveryDTO pDelivery;
        if(ObjUtil.isEmpty(product.getDeliveryId())){
            //init default
            if(ObjUtil.isEmpty(product.getDeliveryPrice())){
                log.debug("product parameter :"+product.toString());
                throw new BusinessException("product delivery price must be defined");
            }

            pDelivery = pDeliveryService.initDefaultPDelivery(product.getSellerNm(), product.getDeliveryPrice());
        }else{
            //get product delivery
            pDelivery = pDeliveryService.getPDeliveryNotNull(product.getDeliveryId());
        }

        //set values
        product.setDeliveryId(pDelivery.getId());
        product.setDeliveryPrice(pDelivery.getPrice());

    }

    public ProductDTO createProduct(ProductDTO product){
        //initialize not defined values
        this.initDefaultParameter(product);

        //verify
        this.verifyParameter(product);

        //insert product
        pDAO.insert(product);

        return this.getProductNotNull(product);
    }

    public ProductDTO getProduct(int id){
        if(id == 0){
            throw new IllegalArgumentException("product id is not defined");
        }

        return pDAO.select(id);
    }

    public List<ProductDTO> getProductsByDelivery(int deliveryId){
        return pDAO.selectByDelivery(deliveryId);
    }

    public ProductDTO updateProduct(ProductDTO product){
        this.getProductNotNull(product);

        //verify
        this.verifyParameter(product);

        //null value will not be updated
        pDAO.update(product);

        return this.getProductNotNull(product);
    }

    public void deleteProduct(int id){
        this.checkProductExist(id);

        pOptionService.deletePOptionByProduct(id);

        pDAO.delete(id);
    }

    public ProductDTO getProductNotNull(int id){
        ProductDTO product = this.getProduct(id);
        if(product == null){
            throw new BusinessException("product is not exists");
        }
        return product;
    }

    public void checkProductExist(int id){
        if(id == 0){
            throw new IllegalArgumentException("product id parameter is not defined");
        }

        if(pDAO.count(id) == 0){
            throw new BusinessException(id+" product is not exists");
        }
    }

    private ProductDTO getProductNotNull(ProductDTO product){
        if(product == null){
            throw new IllegalArgumentException("product is not defined");
        }
        return this.getProductNotNull(product.getId());
    }

    /**
     * verify {@link ProductDTO}
     * only defined values
     * 
     * @param product to be verified
     */
    private void verifyParameter(ProductDTO product){
        //price
        if(ObjUtil.isNotEmpty(product.getPrice())){
            if(product.getPrice() < 0){
                throw new BusinessException("price must be greater than or equal to 0");
            }
        }

        //category
        if(ObjUtil.isNotEmpty(product.getCategoryId())){
            cService.checkCategoryExist(product.getCategoryId());
        }

        //seller
        if(ObjUtil.isNotEmpty(product.getSellerNm())){
            uService.checkUserExist(product.getSellerNm());
        }

        //stock cnt
        if(ObjUtil.isNotEmpty(product.getStockCnt())){
            if(product.getStockCnt() < 0){
                throw new BusinessException("stock count must be greater than or equal to 0");
            }
        }

        //deliveryId
        if(ObjUtil.isNotEmpty(product.getDeliveryId())){
            pDeliveryService.checkPDeliveryExist(product.getDeliveryId());
        }

        //delivery_price
        if(ObjUtil.isNotEmpty(product.getDeliveryPrice())){
            if(product.getDeliveryPrice() < 0){
                throw new BusinessException("delivery price must be greater than or equal to 0");
            }
        }

        //discount_price
        if(ObjUtil.isNotEmpty(product.getDiscountPrice())){
            if(product.getDiscountPrice() < 0){
                throw new BusinessException("discount price must be greater than or equal to 0");
            }
        }
    }
}
