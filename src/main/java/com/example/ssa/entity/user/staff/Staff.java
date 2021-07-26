package com.example.ssa.entity.user.staff;

import com.example.ssa.entity.user.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= StaffConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Staff {
    @Id
    @Column(name = StaffConstants.ID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = StaffConstants.FIRSTNAME)
    private String firstname;

    @Column(name = StaffConstants.SURNAME)
    private String surname;

    @Column(name = StaffConstants.USER_ROLE)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = StaffConstants.MANAGER_ID)
    private long managerId;

    @Column(name = StaffConstants.SKILL_IDS)
    @ElementCollection
    private List<Integer> skillIds;
}
