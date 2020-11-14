package dev.rokong.product.detail;

import java.util.List;

import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.dto.ProductOptionDTO;

public interface ProductDetailService {
    public List<ProductDetailDTO> getDetails(ProductDetailDTO pDetail);
    public ProductDetailDTO getDetail(ProductDetailDTO pDetail);
    public ProductDetailDTO getDetailNotNull(ProductDetailDTO pDetail);
    public ProductDetailDTO createDetail(ProductDetailDTO pDetail);
    public void deleteDetail(ProductDetailDTO pDetail);
    public ProductDetailDTO updateDetail(ProductDetailDTO pDetail);
    
    public List<ProductDetailDTO> getDetailsByOption(ProductOptionDTO pOption);
    public void deleteDetailByOption(ProductOptionDTO pOption);
    public void updateNameByOption(ProductOptionDTO pOption);
}