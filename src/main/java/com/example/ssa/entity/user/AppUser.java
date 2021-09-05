package com.example.ssa.entity.user;

import com.example.ssa.entity.user.constants.AppUserConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

/**
 * All information required for any user in the application.
 * This entity is used for storage of all user information, except for specific details related to their role.
 * @see Manager for more information of the extra fields related to a manager.
 * @see Staff for more information of the extraf fields related to a staff member.
 */
@Entity
@Table(name = AppUserConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AppUser {
    @Id
    @Column(name = AppUserConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = AppUserConstants.FIRSTNAME)
    private String firstname;

    @Column(name = AppUserConstants.SURNAME)
    private String surname;

    @Column(name = AppUserConstants.EMAIL)
    private String email;

    @Column(name = AppUserConstants.PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = AppUserConstants.USER_ROLE)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Formula("CONCAT_WS(' ', firstname, surname)")
    @JsonIgnore
    private String name;
}
