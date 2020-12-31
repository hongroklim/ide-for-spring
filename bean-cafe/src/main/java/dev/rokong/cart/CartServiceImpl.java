package dev.rokong.cart;

import java.util.List;

import dev.rokong.dto.ProductDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.CartDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.product.detail.ProductDetailService;
import dev.rokong.product.main.ProductService;
import dev.rokong.user.UserService;
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartDAO cartDAO;
    @Autowired
    private UserService uService;
    @Autowired
    private ProductService pService;
    @Autowired
    private ProductDetailService pDetailService;

    public List<CartDTO> getCarts(CartDTO cart){
        CartDTO param = new CartDTO(cart);
        //set option cd + %
        String optionCd = param.getOptionCd();
        if(ObjUtil.isNotEmpty(optionCd)){
            param.setOptionCd(optionCd+"%");
        }
        return cartDAO.selectList(param);
    }

    public CartDTO getCart(CartDTO cart){
        this.verifyPrimaryParameter(cart);
        return cartDAO.select(cart);
    }
    
    public CartDTO getCartNotNull(CartDTO cart){
        CartDTO result = this.getCart(cart);
        if(result == null){
            log.debug("cart parameter : "+cart.toString());
            throw new BusinessException("cart is not exists");
        }
        return result;
    }

    public void checkCartExists(CartDTO cart){
        this.verifyPrimaryParameter(cart);

        if (cartDAO.count(cart) == 0) {
            log.debug("cart parameter : {}", cart.toString());
            throw new BusinessException("cart is not exists");
        }
    }
    
    public CartDTO createCart(CartDTO cart){
        this.verifyPrimaryParameter(cart);

        this.verifyCnt(cart.getCnt());

        //user name is exists
        uService.checkUserExist(cart.getUserNm());

        //product is exists
        pService.checkProductExist(cart.getProductId());

        //product cd is exists
        ProductDetailDTO pDetail = new ProductDetailDTO(cart.getProductId(), cart.getOptionCd());
        pDetailService.checkDetailExist(pDetail);

        //insert
        cartDAO.insert(cart);

        return this.getCartNotNull(cart);
    }
    
    public void deleteCarts(CartDTO cart){
        CartDTO param = new CartDTO(cart);
        //set option cd + %
        String optionCd = param.getOptionCd();
        if(ObjUtil.isNotEmpty(optionCd)){
            param.setOptionCd(optionCd+"%");
        }
        cartDAO.delete(cart);
    }

    public void deleteCarts(int productId, String optionCd){
        CartDTO cart = new CartDTO(null, productId, optionCd);
        this.deleteCarts(cart);
    }

    public void deleteCart(CartDTO cart){
        this.checkCartExists(cart);
        cartDAO.delete(cart);
    }
    
    public CartDTO updateCartCnt(CartDTO cart){
        this.checkCartExists(cart);
        
        this.verifyCnt(cart.getCnt());

        cartDAO.updateCnt(cart);
        return this.getCartNotNull(cart);
    }

    private void verifyCnt(Integer cnt){
        if(ObjUtil.isEmpty(cnt)){
            throw new BusinessException("count should be defined");

        }else if(cnt <= 0){
            log.debug("cart parameter : "+cnt);
            throw new BusinessException("count should be exceed 0");

        }
    }

    /**
     * verify CartDTO primary parameter
     * except <code>optionCd</code>
     * (this could be null or empty)
     * 
     * @param cart
     * @throws BusinessException userNm or productId is not defined
     */
    private void verifyPrimaryParameter(CartDTO cart){
        if(ObjUtil.isEmpty(cart.getUserNm())){
            log.debug("cart parameter : "+cart.toString());
            throw new BusinessException("user name is not defined");

        }else if(ObjUtil.isEmpty(cart.getProductId())){
            log.debug("cart parameter : "+cart.toString());
            throw new BusinessException("product id is not defined");

        }
    }
    
}