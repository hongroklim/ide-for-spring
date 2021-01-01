package dev.rokong.cart;

import dev.rokong.dto.CartDTO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/cart", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Cart"})
public class CartController {
    
    @Autowired
    private CartService cartService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ApiOperation(value="get carts", notes = "get user's cart list [userNm]")
    public List<CartDTO> getCarts(@ModelAttribute CartDTO cart){
        return cartService.getCarts(cart);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="create cart",
            notes = "add product in user's cart [userNm, productId, optionCd, cnt]")
    public CartDTO addCart(@RequestBody CartDTO cart){
        return cartService.createCart(cart);
    }

    @RequestMapping(value="", method=RequestMethod.PUT)
    @ApiOperation(value="update count",
            notes = "update product's count [userNm, productId, optionCd, cnt]")
    public CartDTO updateCartCnt(@RequestBody CartDTO cart){
        return cartService.updateCartCnt(cart);
    }

    @RequestMapping(value="", method=RequestMethod.DELETE)
    @ApiOperation(value="delete cart",
            notes = "product id or option cd can be used [userNm, productId, optionCd]")
    public void deleteCarts(@ModelAttribute CartDTO cart){
        cartService.deleteCarts(cart);
    }
}