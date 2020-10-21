package dev.rokong.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userNm;
    private String pwd;
    private boolean enabled;
    private String authority;
}