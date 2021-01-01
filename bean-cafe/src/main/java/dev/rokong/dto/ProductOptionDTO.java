package dev.rokong.dto;

import dev.rokong.util.ObjUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Product Option", description="product's option")
public class ProductOptionDTO {

    @ApiModelProperty(value="product id", example="3", position=1)
    private int productId;

    @ApiModelProperty(value="option group", example="2", position=2)
    private Integer optionGroup;

    @ApiModelProperty(value="option id", example="02", position=3)
    private String optionId;

    @ApiModelProperty(value="title or name", example="for gift", position=4)
    private String name;

    @ApiModelProperty(value="display order in siblings", example="2", position=5)
    private Integer ord;

    public static final String TITLE_ID = "00";

    /**
     * verify whether paramter follows option id sequence like
     * <p/> <code>00 ~ 09</code> -> <code>0A ~ 0Z</code>
     * -> <code>0a ~ 0z</code> -> <code>10 ~ 19</code> ...
     * @param optionId option id to verify
     * @exception IllegalArgumentException invalid option id
     */
    public static void verifyId(String optionId){
        // 00 ~ 09 -> 0A ~ 0Z -> 0a ~ 0z -> 10 ~ 19 ...
        if(optionId == null || "".equals(optionId)){
            throw new IllegalArgumentException("optionId is empty");
        }else if(optionId.length() != 2){
            throw new IllegalArgumentException("optionId's length must be 2");
        }else{
            String tmp = optionId.replaceAll("[0-9A-Za-z]", "");
            if(ObjUtil.isNotEmpty(tmp)){
                throw new IllegalArgumentException("invalid string in optionId : "+tmp);
            }
        }
    }

    public static String nextId(String optionId){
        verifyId(optionId);

        if(optionId.endsWith("9")){
            return optionId.charAt(0)+"A";

        }else if(optionId.endsWith("Z")){
            return optionId.charAt(0)+"a";

        }else if(!optionId.endsWith("z")){
            int ascii = (int) optionId.charAt(1);
            return optionId.charAt(0)
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