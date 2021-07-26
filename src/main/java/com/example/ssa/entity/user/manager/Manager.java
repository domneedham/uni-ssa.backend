package com.example.ssa.entity.user.manager;

import com.example.ssa.entity.user.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= ManagerConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Manager  {
    @Id
    @Column(name = ManagerConstants.ID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = ManagerConstants.FIRSTNAME)
    private String firstname;

    @Column(name = ManagerConstants.SURNAME)
    private String surname;

    @Column(name = ManagerConstants.USER_ROLE)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = ManagerConstants.STAFF_IDS)
    @ElementCollection
    private List<Integer> staffIds;
}
