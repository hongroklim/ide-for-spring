package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductOptionDTO {
    private int productId;
    private Integer optionGroup;
    private String optionId;
    private String name;
    private Integer ord;

    public static String nextId(String optionId){
        // 00 ~ 09 -> 0A ~ 0Z -> 0a ~ 0z -> 10 ~ 19 ...
        if(optionId == null || "".equals(optionId)){
            throw new IllegalArgumentException("optionId is empty");
        }else if(optionId.length() != 2){
            throw new IllegalArgumentException("optionId's length must be 2");
        }else{
            String tmp = optionId.replaceAll("[0-9A-Za-z]", "");
            if(tmp != null && !"".equals(tmp)){
                throw new IllegalArgumentException("invalid string in optionId : "+tmp);
            }
        }

        if(optionId.endsWith("9")){
            return optionId.substring(0, 1)+"A";

        }else if(optionId.endsWith("Z")){
            return optionId.substring(0, 1)+"a";

        }else if(!optionId.endsWith("z")){
            int ascii = (int) optionId.charAt(1);
            return optionId.substring(0, 1)
                    + Character.toString((char) ++ascii);
            
        } else{//if(optionId.endsWith("z"))
            if(optionId.startsWith("9")){
                return "A0";

            }else if(optionId.startsWith("Z")){
                return "a0";

            }else if(!optionId.startsWith("z")){
                int ascii = (int) optionId.charAt(0);
                return Character.toString((char) ++ascii)+"0";

            }else {//if(optionId.startsWith("z"))
                return "00";
            }
        }
    }

    public ProductOptionDTO(int pId){
        this.productId = pId;
    }

    public ProductOptionDTO(int pId, int groupId){
        this.productId = pId;
        this.optionGroup = groupId;
    }

    public ProductOptionDTO(int pId, int groupId, String optionId){
        this.productId = pId;
        this.optionGroup = groupId;
        this.optionId = optionId;
    }

    public ProductOptionDTO(int pId, int groupId, String optionId, int ord){
        this.productId = pId;
        this.optionGroup = groupId;
        this.optionId = optionId;
        this.ord = ord;
    }

    public ProductOptionDTO(ProductOptionDTO pOption){
        this.productId = pOption.getProductId();
        this.optionGroup = pOption.getOptionGroup();
        this.optionId = pOption.getOptionId();
        this.ord = pOption.getOrd();
    }
}