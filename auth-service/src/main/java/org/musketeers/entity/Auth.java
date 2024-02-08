package org.musketeers.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.entity.enums.EGender;
import org.musketeers.entity.enums.EStatus;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tbl_auth")
public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    private String phone;
    private String activationCode;
    private EGender gender;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EStatus status = EStatus.PENDING;
}
