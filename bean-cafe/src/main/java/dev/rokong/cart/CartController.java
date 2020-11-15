package dev.rokong.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.CartDTO;

@RestController
@RequestMapping("/cart/user/{userNm}")
public class CartController {
    
    @Autowired CartService cartService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CartDTO> getCarts(@PathVariable String userNm){
        CartDTO param = new CartDTO(userNm);
        return cartService.getCarts(param);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public CartDTO addCart(@RequestBody CartDTO cart){
        return cartService.createCart(cart);
    }

    @RequestMapping(value="", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCartAll(@PathVariable String userNm){
        CartDTO param = new CartDTO(userNm);
        cartService.deleteCarts(param);
    }

    @RequestMapping(value="/product/{pId}/option/{oCd}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCart(@PathVariable String userNm, @PathVariable int pId,
            @PathVariable String oCd){
        CartDTO param = new CartDTO(userNm, pId, oCd);
        cartService.deleteCart(param);
    }

    @RequestMapping(value="/product/{pId}/option/{oCd}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public CartDTO updateCartCnt(@PathVariable String userNm, @PathVariable int pId,
            @PathVariable String oCd, @RequestBody CartDTO cart){
        CartDTO param = new CartDTO(userNm, pId, oCd);
        param.setCnt(cart.getCnt());
        return cartService.updateCartCnt(param);
    }

}