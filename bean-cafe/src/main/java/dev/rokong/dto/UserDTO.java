package dev.rokong.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@JsonIgnoreProperties({"authorities", "username", "password"})
public class UserDTO {
    private String userNm;
    private String pwd;
    private Boolean enabled;
    private List<String> authority;

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(this.authority != null && this.authority.size() > 0){
            for(String role : authority){
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities){
        this.authority = new ArrayList<>();
        for(GrantedAuthority auth : authorities){
            this.authority.add(auth.getAuthority());
        }
    }

}