package com.example.security_service.dto;

import com.example.security_service.entity.MemberCredentialsModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomMemberDetails implements UserDetails {
    private String userName;
    private String password;
    private boolean isFirstUser;

    public CustomMemberDetails(MemberCredentialsModel memberCredentialsModel) {
        this.userName = memberCredentialsModel.getUserName();
        this.password = memberCredentialsModel.getPassword();
        this.isFirstUser = memberCredentialsModel.isFirstUser();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
