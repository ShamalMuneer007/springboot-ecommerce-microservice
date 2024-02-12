package org.shamal.identityservice.services.security;

import lombok.extern.slf4j.Slf4j;
import org.shamal.identityservice.config.security.CustomUserDetails;
import org.shamal.identityservice.model.entities.UserIdentity;
import org.shamal.identityservice.services.UserIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {



    @Autowired
    private UserIdentityService userIdentityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserIdentity userIdentity;
        try{
           userIdentity =  userIdentityService.getUserIdentityByUsername(username);
        }
        catch (Exception e){
            throw new UsernameNotFoundException("Invalid username");
        }
        return new CustomUserDetails(userIdentity);
    }
}
