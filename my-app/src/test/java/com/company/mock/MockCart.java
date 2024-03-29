package com.company.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.cart.CartService;
import com.company.dto.CartDTO;
import com.company.dto.ProductDetailDTO;
import com.company.util.RandomUtil;

@Component("MockCart")
public class MockCart extends AbstractMockObject<CartDTO> {
    
    @Autowired CartService cService;

    @Autowired MockUser mUser;
    @Autowired MockProductDetail mPDetail;

    @Override
    public CartDTO temp() {
        CartDTO temp = new CartDTO();
        
        temp.setUserNm(mUser.any().getUserNm());
        temp.setProductId(mPDetail.any().getProductId());
        temp.setOptionCd(mPDetail.any().getOptionCd());
        temp.setCnt(RandomUtil.randomInt(1)+1);

        return temp;
    }

    @Override
    protected CartDTO createObjService(CartDTO obj) {
        return cService.createCart(obj);
    }

    @Override
    protected CartDTO getObjService(CartDTO obj) {
        return cService.getCart(obj);
    }

    @Override
    protected CartDTO tempNth(int i){
        CartDTO cart = this.temp();
        ProductDetailDTO pDetail = mPDetail.anyList(i+1).get(i);

        cart.setProductId(pDetail.getProductId());
        cart.setOptionCd(pDetail.getOptionCd());

        return cart;
    }

}