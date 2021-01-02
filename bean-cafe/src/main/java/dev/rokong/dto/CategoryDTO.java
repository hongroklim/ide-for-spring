package dev.rokong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Category", description="products are classified in this category")
public class CategoryDTO {

    @ApiModelProperty(value="category id", example="3", position=1)
    private int id;

    @ApiModelProperty(value="category name", example="africa", position=2)
    private String name;

    @ApiModelProperty(value="parent's id", example="7", position=3)
    private Integer upId;

    @ApiModelProperty(value="display order in siblings", example="2", allowableValues="range[1, infinity]", position=4)
    private Integer ord;

    public static final int ROOT_ID = 0;
    public static final int ETC_ID = 1;

    public CategoryDTO(int id){
        this.id = id;
    }
}