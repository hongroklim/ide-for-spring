package dev.rokong.dto;

public class UserDTO {
    private String userNm;
    private String password;
    private boolean enabled;
    private String authority;

    public String getUserNm() {
        return userNm;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
}