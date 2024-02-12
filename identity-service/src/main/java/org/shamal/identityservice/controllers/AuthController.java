package org.shamal.identityservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.shamal.identityservice.model.dtos.AuthResponseDto;
import org.shamal.identityservice.model.dtos.UserIdentityDto;
import org.shamal.identityservice.services.UserIdentityService;
import org.shamal.identityservice.services.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserIdentityService userIdentityService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserIdentityService userIdentityService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userIdentityService = userIdentityService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> userLogin(@RequestBody UserIdentityDto userDto){
        log.info("Authenticating user");
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            AuthResponseDto authResponseDto = new AuthResponseDto();
            authResponseDto.setToken(jwtService.generateToken(authentication));
            return new ResponseEntity<>(authResponseDto,HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            log.error("Something went wrong while authenticating user");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
    @PostMapping("/register")
    public ResponseEntity<String> userRegister(@RequestBody UserIdentityDto userIdentityDto){
        try {
            userIdentityService.registerUser(userIdentityDto);
        }
        catch (Exception e){
            log.error("Something went wrong while registering user {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User Registered successfully",HttpStatus.OK);
    }
}
