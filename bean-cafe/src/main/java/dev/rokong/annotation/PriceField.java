package dev.rokong.annotation;

import java.lang.reflect.Method;

import dev.rokong.dto.ProductDTO;
import dev.rokong.exception.ApplicationException;
import lombok.Getter;

/**
 * class of price in productDTO
 */
@Getter
public enum PriceField {
    price("price", "price"),
    delivery("deliveryPrice", "delivery_price"),
    discount("discountPrice", "discount_price");

    private String dtoName;
    private String dbName;

    PriceField(String dtoName, String dbName) {
        this.dtoName = dtoName;
        this.dbName = dbName;
    }

    public String getMethodName() {
        return "get" + this.dtoName.substring(0, 1).toUpperCase()
                + this.dtoName.substring(1);
    }

    public int getValueFrom(ProductDTO product) {
        int result = 0;
        
        if (product == null) {
            return result;
        }

        Class<ProductDTO> clazz = ProductDTO.class;
        
        try {
            Method m = clazz.getMethod(this.getMethodName());
            result = (int) m.invoke(product);
        } catch (Exception e) {
            throw new ApplicationException(
                        "fail to execute "+this.getMethodName()
                        +" in PriceField", e);
        }

        return result;
    }
}