package dev.rokong.product.main;

import java.util.List;

import dev.rokong.dto.ProductDTO;

public interface ProductService {
    public List<ProductDTO> getProductList();
    public ProductDTO getProduct(int id);
    public ProductDTO getProductNotNull(int id);
    public void checkProductExist(int id);
    public ProductDTO createProduct(ProductDTO product);
    public List<ProductDTO> getProductsByDelivery(int deliveryId);
    public ProductDTO updateProduct(ProductDTO product);
    public void deleteProduct(int id);
}