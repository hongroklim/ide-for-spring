package dev.rokong.cart;

import java.util.List;

import dev.rokong.dto.CartDTO;

public interface CartService {
    public List<CartDTO> getCarts(CartDTO cart);
    public CartDTO getCart(CartDTO cart);
    public CartDTO getCartNotNull(CartDTO cart);
    public CartDTO createCart(CartDTO cart);
    public void deleteCarts(CartDTO cart);
    public void deleteCarts(int productId, String optionCd);
    public void deleteCart(CartDTO cart);
    public CartDTO updateCartCnt(CartDTO cart);
}