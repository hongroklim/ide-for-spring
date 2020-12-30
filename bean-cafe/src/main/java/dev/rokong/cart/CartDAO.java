package dev.rokong.cart;

import java.util.List;

import dev.rokong.dto.CartDTO;

public interface CartDAO {
    public List<CartDTO> selectList(CartDTO cart);
    public CartDTO select(CartDTO cart);
    public int count(CartDTO cart);
    public void insert(CartDTO cart);
    public void delete(CartDTO cart);
    public void updateCnt(CartDTO cart);
}