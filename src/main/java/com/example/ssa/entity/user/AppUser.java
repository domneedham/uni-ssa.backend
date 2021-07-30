package com.example.ssa.entity.user;

import com.example.ssa.entity.user.constants.AppUserConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = AppUserConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AppUser {
    @Id
    @Column(name = AppUserConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = AppUserConstants.FIRSTNAME)
    private String firstname;

    @Column(name = AppUserConstants.SURNAME)
    private String surname;

    @Column(name = AppUserConstants.EMAIL)
    private String email;

    @Column(name = AppUserConstants.USER_ROLE)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Formula("CONCAT_WS(' ', firstname, surname)")
    @JsonIgnore
    private String name;
}
