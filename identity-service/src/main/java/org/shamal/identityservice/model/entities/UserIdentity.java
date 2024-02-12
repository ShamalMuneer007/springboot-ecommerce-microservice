package org.shamal.identityservice.model.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.Type;
import org.shamal.identityservice.enums.ROLE;

import java.util.UUID;

@Entity
@Table(name = "user_identity")
@Getter
@Setter
public class UserIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID identity_id;
//    @Column(nullable = false)
    private String username;
//    @Column(nullable = false)
    private String password;
    private String email;
    @Enumerated(value = EnumType.STRING)
//    @Column(nullable = false)
    private ROLE role;

}
