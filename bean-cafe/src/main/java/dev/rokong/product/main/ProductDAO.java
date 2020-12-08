package dev.rokong.product.main;

import java.util.List;

import dev.rokong.dto.ProductDTO;

public interface ProductDAO {
    public List<ProductDTO> selectProductList();
    public ProductDTO selectProduct(int id);
    public List<ProductDTO> selectProductsByDelivery(int deliveryId);
    public int insertProduct(ProductDTO product);
    public void deleteProduct(int id);
    public void updateProduct(ProductDTO product);
    public void updateProductColumn(int id, String column, Object value);
    public void updateProductCategory(int asisCategory, int tobeCategory);
}
