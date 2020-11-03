package dev.rokong.product.main;

import java.util.List;

import dev.rokong.annotation.PriceField;
import dev.rokong.dto.ProductDTO;

public interface ProductService {
    public List<ProductDTO> getProductList();
    public ProductDTO createProduct(ProductDTO product);
    public ProductDTO getProduct(int id);
    public ProductDTO getProductNotNull(int id);
    public ProductDTO updateProduct(ProductDTO product);
    public void deleteProductStock(int id);
    public void deleteProduct(int id);
    public ProductDTO updateProductPrice(ProductDTO product, PriceField field);
}