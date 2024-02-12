package org.shamal.identityservice.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shamal.identityservice.model.entities.UserIdentity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserIdentityDto {
    private String username;
    private String password;
    private String email;
    UserIdentityDto(UserIdentity userIdentity){
        this.username = userIdentity.getUsername();
        this.password = userIdentity.getPassword();
        this.email = userIdentity.getEmail();
    }
}
