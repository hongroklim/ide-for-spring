package com.company.product.option;

import java.util.List;

import com.company.dto.ProductOptionDTO;

public interface ProductOptionDAO {
    public List<ProductOptionDTO> selectProductOptionList(ProductOptionDTO pOption);
    public ProductOptionDTO selectProductOption(ProductOptionDTO pOption);
    public void insertProductOption(ProductOptionDTO pOption);
    public void deleteProductOption(ProductOptionDTO pOption);
    public void updateProductOption(ProductOptionDTO asisPOption, String name, int ord);
    public void backwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder);
    public void forwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder);
    public void updateOptionGroup(ProductOptionDTO pOption, int tobeGroup);
    public void backwardOptionGroup(int productId, int startGroup, int endGroup);
    public void forwardOptionGroup(int productId, int startGroup, int endGroup);
}