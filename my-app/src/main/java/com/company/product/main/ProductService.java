package com.company.product.main;

import java.util.List;

import com.company.dto.ProductDTO;

public interface ProductService {
    public List<ProductDTO> getProductList();
    public ProductDTO createProduct(ProductDTO product);
    public ProductDTO getProduct(int id);
    public ProductDTO getProductNotNull(int id);
    public List<ProductDTO> getProductsByDelivery(int deliveryId);
    // TODO public boolean isProductExists(int id);
    public ProductDTO updateProduct(ProductDTO product);
    public void deleteProduct(int id);
}