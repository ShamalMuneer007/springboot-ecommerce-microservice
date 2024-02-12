package org.shamal.identityservice.services;

import lombok.extern.slf4j.Slf4j;
import org.shamal.identityservice.exceptions.InvalidInputException;
import org.shamal.identityservice.model.dtos.RegisterNotificationMessageDto;
import org.shamal.identityservice.model.dtos.UserIdentityDto;
import org.shamal.identityservice.model.entities.UserIdentity;
import org.shamal.identityservice.enums.ROLE;
import org.shamal.identityservice.repositories.UserIdentityRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@Slf4j
public class UserIdentityService {
    private final PasswordEncoder passwordEncoder;
    private final UserIdentityRepository userIdentityRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;


    public UserIdentityService(PasswordEncoder passwordEncoder,
                               UserIdentityRepository userIdentityRepository,
                               KafkaTemplate<String, String> kafkaTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.userIdentityRepository = userIdentityRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public UserIdentity getUserIdentityByUsername(String username){
        UserIdentity userIdentity;
        try{
            userIdentity =  userIdentityRepository.findByUsername(username);
        }
        catch (Exception e){
            throw new RuntimeException();
        }
        return userIdentity;
    }

    @Transactional
    public void registerUser(UserIdentityDto userIdentityDto) {
        try{
            if(userIdentityRepository.existsByUsername(userIdentityDto.getUsername())){
                log.warn("Username already exists");
                throw new InvalidInputException("Username already exists");
            }
            log.info("Registering a user....");
            validateUserIdentityDto(userIdentityDto);
            UserIdentity userIdentity =  new UserIdentity();
            if(userIdentityRepository.count()<1){
                log.info("Role set to admin");
                userIdentity.setRole(ROLE.ROLE_ADMIN);
            }
            else{
                log.info("Role set to user");
                userIdentity.setRole(ROLE.ROLE_USER);
            }
            userIdentity.setUsername(userIdentityDto.getUsername());
            userIdentity.setPassword(passwordEncoder.encode(userIdentityDto.getPassword()));
            UUID userId = userIdentityRepository.save(userIdentity).getIdentity_id();
            RegisterNotificationMessageDto registerNotificationMessageDto =
                    RegisterNotificationMessageDto.builder()
                    .userId(userId.toString())
                    .email(userIdentityDto.getEmail())
                    .build();
            log.info("Producing message to topic identity");
            try {
                kafkaTemplate.send("identity", userIdentityDto.getEmail());
            }
            catch (Exception e){
                log.error("Something went wrong while sending the message to topic identity");
                throw new RuntimeException(e.getMessage());
            }
            log.info("message send to topic identity successfully");
            log.info("User registered successfully");
        }
        catch (InvalidInputException e){
            throw new InvalidInputException(e.getMessage());
        }
        catch (Exception e){
            log.error("Something went wrong while registering the user");
            throw new RuntimeException("Something went wrong while saving the user identity \n"+e.getMessage());
        }
    }
    private void validateUserIdentityDto(UserIdentityDto userIdentityDto){
        if(userIdentityDto == null) {
            log.warn("User identity cannot be null");
            throw new InvalidInputException("User identity cannot be null");
        }

        if(userIdentityDto.getUsername().isEmpty()) {
            log.warn("Username cannot be empty");
            throw new InvalidInputException("Username cannot be empty");
        }

        if(userIdentityDto.getPassword().isEmpty()) {
            log.warn("Password cannot be empty");
            throw new InvalidInputException("Password cannot be empty");
        }

        if(!isValidPassword(userIdentityDto.getPassword())) {
            log.warn("Invalid password");
            throw new InvalidInputException("Invalid password");
        }
    }
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,128}$"; // 8-128 characters, one lowercase, one uppercase, one digit, one special character
        if (!password.matches(regex)) {
            log.warn("Password must meet complexity requirements: 8-128 characters, 1 lowercase, 1 uppercase, 1 digit, 1 special character.");
            throw new InvalidInputException("Password must meet complexity requirements: 8-128 characters, 1 lowercase, 1 uppercase, 1 digit, 1 special character.");
        }
        if (password.length() < 8) {
            log.warn("Password must be at least 8 characters long.");
            throw new InvalidInputException("Password must be at least 8 characters long.");
        }
        if (password.length() > 128) {
            log.warn("Password cannot exceed 128 characters.");
            throw new InvalidInputException("Password cannot exceed 128 characters.");
        }
        return true;
    }
}
