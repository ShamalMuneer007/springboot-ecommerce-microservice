package org.shamal.identityservice.model.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterNotificationMessageDto {
    private String email;
    private String userId;
}
