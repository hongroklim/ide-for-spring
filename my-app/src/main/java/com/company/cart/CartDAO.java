package com.company.cart;

import java.util.List;

import com.company.dto.CartDTO;

public interface CartDAO {
    public List<CartDTO> selectCarts(CartDTO cart);
    public CartDTO selectCart(CartDTO cart);
    public void insertCart(CartDTO cart);
    public void deleteCart(CartDTO cart);
    public void updateCartCnt(CartDTO cart);
}