package dev.rokong.product.option;

import java.util.List;

import dev.rokong.dto.ProductOptionDTO;

public interface ProductOptionDAO {
    public List<ProductOptionDTO> selectProductOptionList(ProductOptionDTO pOption);
    public ProductOptionDTO selectProductOption(ProductOptionDTO pOption);
    public void insertProductOption(ProductOptionDTO pOption);
    public void deleteProductOption(ProductOptionDTO pOption);
    public void updateProductOption(ProductOptionDTO asisPOption, String optionId, String name);
}