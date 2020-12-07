package dev.rokong.cart;

import java.util.List;

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
    
    @Autowired CartDAO cartDAO;

    @Autowired UserService uService;
    @Autowired ProductService pService;
    @Autowired ProductDetailService pDetailService;

    public List<CartDTO> getCarts(CartDTO cart){
        CartDTO param = new CartDTO(cart);
        //set option cd + %
        String optionCd = param.getOptionCd();
        if(ObjUtil.isNotEmpty(optionCd)){
            param.setOptionCd(optionCd+"%");
        }
        return cartDAO.selectCarts(param);
    }

    public CartDTO getCart(CartDTO cart){
        this.verifyPrimaryParameter(cart);
        return cartDAO.selectCart(cart);
    }
    
    public CartDTO getCartNotNull(CartDTO cart){
        CartDTO result = this.getCart(cart);
        if(result == null){
            log.debug("cart parameter : "+cart.toString());
            throw new BusinessException("cart is not exists");
        }
        return result;
    }
    
    public CartDTO createCart(CartDTO cart){
        this.verifyPrimaryParameter(cart);

        this.verifyCnt(cart.getCnt());

        //user name is exists
        uService.getUserNotNull(cart.getUserNm());

        //product is exists
        pService.getProductNotNull(cart.getProductId());

        //product cd is exists
        pDetailService.getDetailNotNull(cart.getProductId(), cart.getOptionCd());

        //insert
        cartDAO.insertCart(cart);

        return this.getCartNotNull(cart);
    }
    
    public void deleteCarts(CartDTO cart){
        CartDTO param = new CartDTO(cart);
        //set option cd + %
        String optionCd = param.getOptionCd();
        if(ObjUtil.isNotEmpty(optionCd)){
            param.setOptionCd(optionCd+"%");
        }
        cartDAO.deleteCart(cart);
    }

    public void deleteCarts(int productId, String optionCd){
        CartDTO cart = new CartDTO(null, productId, optionCd);
        this.deleteCarts(cart);
    }

    public void deleteCart(CartDTO cart){
        this.getCartNotNull(cart);
        cartDAO.deleteCart(cart);
    }
    
    public CartDTO updateCartCnt(CartDTO cart){
        this.getCartNotNull(cart);
        
        this.verifyCnt(cart.getCnt());

        cartDAO.updateCartCnt(cart);
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