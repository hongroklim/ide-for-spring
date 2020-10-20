package dev.rokong.dto;

public class ProductOptionDTO {
    private int productId;
    private int optionGroup;
    private String optionId;
    private String name;

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(int optionGroup) {
        this.optionGroup = optionGroup;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}