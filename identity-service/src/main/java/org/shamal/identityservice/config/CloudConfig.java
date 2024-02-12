package org.shamal.identityservice.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("jwt")
@Component
@Getter
@Setter
public class CloudConfig {
    private long jwtExpiration;
    private String jwtSecret;
}
