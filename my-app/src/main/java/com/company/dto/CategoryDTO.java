package com.company.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private int id;
    private String name;
    private int upId;
    private int ord;

    public static final int ROOT_ID = 0;
    public static final int ETC_ID = 1;

    public CategoryDTO(int id){
        this.id = id;
    }
}