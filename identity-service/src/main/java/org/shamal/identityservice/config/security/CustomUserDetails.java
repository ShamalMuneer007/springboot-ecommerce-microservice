package org.shamal.identityservice.config.security;

import lombok.extern.slf4j.Slf4j;
import org.shamal.identityservice.model.entities.UserIdentity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Slf4j
public class CustomUserDetails implements UserDetails {
    UserIdentity userIdentity;
    public CustomUserDetails(){}
   public CustomUserDetails(UserIdentity userIdentity){
       this.userIdentity = userIdentity;
   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userIdentity.getRole().name()));
    }

    @Override
    public String getPassword() {
        return userIdentity.getPassword();
    }

    @Override
    public String getUsername() {
        return userIdentity.getUsername();
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
