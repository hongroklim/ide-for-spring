package dev.rokong.product.option;

import java.util.List;

import dev.rokong.dto.ProductOptionDTO;

public interface ProductOptionDAO {
    public List<ProductOptionDTO> selectList(ProductOptionDTO pOption);
    public ProductOptionDTO select(ProductOptionDTO pOption);
    public void insert(ProductOptionDTO pOption);
    public void delete(ProductOptionDTO pOption);

    /**
     * update name, ord in ProductOptionDTO
     * @param pOption productId, optionGroup, optionId must be defined
     */
    public void update(ProductOptionDTO pOption);
    public void backwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder);
    public void forwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder);
    public void updateOptionGroup(ProductOptionDTO pOption, int tobeGroup);
    public void backwardOptionGroup(int productId, int startGroup, int endGroup);
    public void forwardOptionGroup(int productId, int startGroup, int endGroup);
}