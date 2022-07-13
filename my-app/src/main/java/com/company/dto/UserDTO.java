package com.company.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@JsonIgnoreProperties({"authorities", "username", "password"})
@SuppressWarnings("serial")
public class UserDTO implements UserDetails {
    private String userNm;
    private String pwd;
    private boolean enabled;
    private List<String> authority;

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.userNm;
    }

    @Override
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

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}