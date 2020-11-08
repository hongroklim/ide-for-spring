package dev.rokong.product.option;

import java.util.List;

import dev.rokong.dto.ProductOptionDTO;

public interface ProductOptionService {
    public List<ProductOptionDTO> getPOptionList(ProductOptionDTO pOption);
    public ProductOptionDTO getPOption(ProductOptionDTO pOption);
    public ProductOptionDTO getPOptionNotNull(ProductOptionDTO pOption);
    public ProductOptionDTO createPOptionGroup(ProductOptionDTO pOption);
    public ProductOptionDTO createPOption(ProductOptionDTO pOption);
    public void deletePOption(ProductOptionDTO pOption);
    public ProductOptionDTO updatePOption(ProductOptionDTO asisPOption, ProductOptionDTO tobePOption);
    public void deletePOptionGroup(ProductOptionDTO pOption);
    public void deletePOptionAll(int productId);
    public void updatePOptionGroupOrder(ProductOptionDTO asisPOption, int tobePOptionGroup);
}
