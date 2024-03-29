package com.company.product.detail;

import java.util.List;

import com.company.dto.ProductDetailDTO;

public interface ProductDetailDAO {
    public List<ProductDetailDTO> selectDetailList(ProductDetailDTO pDetail);
    public ProductDetailDTO selectDetail(ProductDetailDTO pDetail);
    public void insertDetail(ProductDetailDTO pDetail);
    public void deleteDetail(ProductDetailDTO pDetail);
    public void deleteDetailList(ProductDetailDTO pDetail);
    public void updateDetail(ProductDetailDTO pDetail);
}