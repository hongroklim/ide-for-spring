package dev.rokong.product.main;

import java.util.List;

import dev.rokong.dto.ProductDTO;

public interface ProductDAO {
    public List<ProductDTO> selectList();
    public ProductDTO select(int id);
    public List<ProductDTO> selectByDelivery(int deliveryId);
    public int count(int id);
    public int insert(ProductDTO product);
    public void delete(int id);
    public void update(ProductDTO product);
    public void updateColumn(int id, String column, Object value);
    public void updateCategory(int asisCategory, int tobeCategory);
}
