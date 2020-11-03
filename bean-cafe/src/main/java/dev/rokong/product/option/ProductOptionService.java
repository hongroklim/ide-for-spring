package dev.rokong.product.option;

import java.util.List;

import dev.rokong.dto.ProductOptionDTO;

public interface ProductOptionService {
    public List<ProductOptionDTO> getPOptionList(ProductOptionDTO pOption);
    public ProductOptionDTO getPOption(ProductOptionDTO pOption);
    public ProductOptionDTO getPOptionNotNull(ProductOptionDTO pOption);
}