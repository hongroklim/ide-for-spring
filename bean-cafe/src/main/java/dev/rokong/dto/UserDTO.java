package dev.rokong.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"authorities", "username", "password"})
@ApiModel(value="User", description="users")
public class UserDTO {

    @ApiModelProperty(value="user name", example="customer1", position=1)
    private String userNm;

    @ApiModelProperty(value="password", example="customer1", position=2)
    private String pwd;

    @ApiModelProperty(value="is enabled", example="true", position=3)
    private Boolean enabled;

    @ApiModelProperty(value="user authorities", example="", position=4)
    private List<String> authority;

    @ApiModelProperty(hidden = true)
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(this.authority != null && this.authority.size() > 0){
            for(String role : authority){
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        
        return authorities;
    }

    @ApiModelProperty(hidden = true)
    public void setAuthorities(List<GrantedAuthority> authorities){
        this.authority = new ArrayList<>();
        for(GrantedAuthority auth : authorities){
            this.authority.add(auth.getAuthority());
        }
    }

}