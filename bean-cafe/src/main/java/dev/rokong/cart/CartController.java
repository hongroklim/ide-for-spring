package dev.rokong.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.CartDTO;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired CartService cartService;

    @RequestMapping(value="/user/{userNm}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CartDTO> getCarts(@PathVariable String userNm){
        return null;
    }

    @RequestMapping(value="/user/{userNm}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public CartDTO addCarts(@PathVariable String userNm){
        return null;
    }

}