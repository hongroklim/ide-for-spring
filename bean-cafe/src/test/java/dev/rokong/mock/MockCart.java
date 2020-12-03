package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.cart.CartService;
import dev.rokong.dto.CartDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.util.RandomUtil;

@Component("MockCart")
public class MockCart {
    
    private List<CartDTO> cartList = new ArrayList<CartDTO>();

    @Autowired private CartService cService;

    @Autowired MockUser user;
    @Autowired MockProductDetail pDetail;

    public CartDTO tempCart(){
        CartDTO temp = new CartDTO();
        temp.setUserNm(user.anyUser().getUserNm());
        temp.setProductId(pDetail.anyPDetail().getProductId());
        temp.setOptionCd(pDetail.anyPDetail().getOptionCd());
        temp.setCnt(RandomUtil.randomInt(1));
        return temp;
    }

    public CartDTO createCart(){
        CartDTO cart = this.tempCart();
        return cService.createCart(cart);
    }

    public CartDTO anyCart(){
        this.validatingList();

        if(this.cartList.size() == 0){
            cartList.add(this.createCart());
        }
        return cartList.get(0);
    }

    public List<CartDTO> anyCartList(int count){
        this.validatingList();
        this.appendCartListUntil(count);
        return this.cartList.subList(0, count);
    }

    private void appendCartListUntil(int count){
        List<ProductDetailDTO> detailList = pDetail.anyPDetailList(count);
        CartDTO cart = this.tempCart();

        for(int i=this.cartList.size(); i<count; i++){
            cart.setProductId(detailList.get(i).getProductId());
            cart.setOptionCd(detailList.get(i).getOptionCd());
            this.cartList.add(cService.createCart(cart));
        }
    }

    private boolean isValidList(){
        if(this.cartList.size() == 0){
            return true;
        }else{
            return cService.getCart(this.cartList.get(0)) != null;
        }
    }

    private void validatingList(){
        if(!this.isValidList()){
            this.cartList.clear();
        }
    }

}