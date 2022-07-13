package cart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import com.company.cart.CartController;
import com.company.cart.CartService;
import com.company.dto.CartDTO;
import com.company.util.ObjUtil;

public class ControllerTest extends MvcUnitConfig {

    @Autowired CartController cController;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(cController).build();
    }
    
    @Autowired CartService cService;

    private CartDTO anyCart;

    @Before
    public void initAnyCart(){
        this.anyCart = mockObj.cart.any();
    }

    @Test
    public void mockCartTest(){
        CartDTO cart = mockObj.cart.any();
        assertThat(cart, is(notNullValue()));

        List<CartDTO> cartList = mockObj.cart.anyList(3);
        assertThat(ObjUtil.isNotEmpty(cartList), is(equalTo(true)));
        assertThat(cartList.size(), is(equalTo(3)));
    }

    private String cartURL(CartDTO cart){   
        assertThat(cart, is(notNullValue()));
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("/cart/user/");

        //set userNm 
        assertThat(ObjUtil.isNotEmpty(cart.getUserNm()), is(equalTo(true)));
        sbuf.append(cart.getUserNm());

        if(ObjUtil.isNotEmpty(cart.getProductId())){
            //if productId defined, set product id and option cd
            sbuf.append("/product/")
                .append(cart.getProductId())
                .append("/option/")
                .append(cart.getOptionCd());
        }

        return sbuf.toString();
    }

    @Test
    public void getCarts() throws Exception{
        CartDTO param = new CartDTO(this.anyCart.getUserNm());
        List<CartDTO> res = this.reqAndResBodyList(
            this.cartURL(param), RequestMethod.GET, null, CartDTO.class
        );

        assertThat(ObjUtil.isNotEmpty(res), is(equalTo(true)));
        assertThat(res.get(0).getUserNm(), is(equalTo(param.getUserNm())));
    }

    @Test
    public void addCart() throws Exception {
        CartDTO req = this.anyCart;
        req.setUserNm(mockObj.user.anyList(2).get(1).getUserNm());

        CartDTO res = this.reqAndResBody(
            this.cartURL(new CartDTO(req.getUserNm())),
            RequestMethod.POST, req, CartDTO.class
        );

        assertThat(res, is(notNullValue()));

        //response's primary keys are equal to request ones
        assertThat(res.getUserNm(), is(equalTo(req.getUserNm())));
        assertThat(res.getProductId(), is(equalTo(req.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(req.getOptionCd())));
    }

    @Test
    public void deleteCartAll() throws Exception{
        //append cart list to 3
        mockObj.cart.anyList(3);
        
        CartDTO param = new CartDTO(mockObj.cart.any().getUserNm());
        List<CartDTO> list = cService.getCarts(param);
        
        assertThat(ObjUtil.isNotEmpty(list), is(equalTo(true)));
        assertThat(list.size(), is(greaterThanOrEqualTo(3)));

        this.reqAndResBody(this.cartURL(param), RequestMethod.DELETE, null, null);

        list = cService.getCarts(param);
        assertThat(ObjUtil.isEmpty(list), is(equalTo(true)));
    }

    @Test
    public void deleteCart() throws Exception{
        CartDTO result = cService.getCart(this.anyCart);
        assertThat(result, is(notNullValue()));

        this.reqAndResBody(this.cartURL(this.anyCart), RequestMethod.DELETE, null, null);
        
        result = cService.getCart(this.anyCart);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void updateCartCnt() throws Exception{
        CartDTO asisCart = this.anyCart;
        CartDTO req = cService.getCartNotNull(asisCart);
        int tobeCnt = req.getCnt()+10;
        req.setCnt(tobeCnt);

        CartDTO res = this.reqAndResBody(
            this.cartURL(req), RequestMethod.PUT, req, CartDTO.class
        );

        assertThat(res, is(notNullValue()));
        //response's primary keys are equal to request ones
        assertThat(res.getUserNm(), is(equalTo(asisCart.getUserNm())));
        assertThat(res.getProductId(), is(equalTo(asisCart.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(asisCart.getOptionCd())));

        assertThat(res.getCnt(), is(equalTo(tobeCnt)));
    }
}