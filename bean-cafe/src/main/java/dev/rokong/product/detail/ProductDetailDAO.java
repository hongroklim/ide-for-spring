package dev.rokong.product.detail;

import java.util.List;

import dev.rokong.dto.ProductDetailDTO;

public interface ProductDetailDAO {
    public List<ProductDetailDTO> selectList(ProductDetailDTO pDetail);
    public ProductDetailDTO select(ProductDetailDTO pDetail);
    public int count(ProductDetailDTO pDetail);
    public void insert(ProductDetailDTO pDetail);
    public void delete(ProductDetailDTO pDetail);
    public void deleteList(ProductDetailDTO pDetail);
    public void update(ProductDetailDTO pDetail);
}