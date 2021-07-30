package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.ManagerStaffSkillConstants;
import com.example.ssa.entity.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = ManagerStaffSkillConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ManagerStaffSkill {
    @Id
    @Column(name = ManagerStaffSkillConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne()
    @JoinColumn(name = ManagerStaffSkillConstants.ID, insertable = false, updatable = false)
    private Skill skill;

    @JoinTable(
        name = ManagerStaffSkillConstants.STAFF_DETAILS_TABLE_LINK,
        joinColumns=@JoinColumn(name = ManagerStaffSkillConstants.STAFF_DETAILS_JOIN_COLUMNS),
        inverseJoinColumns=@JoinColumn(name = ManagerStaffSkillConstants.STAFF_DETAILS_INVERSE_JOIN_COLUMNS)
    )
    @OneToMany()
    private List<AppUser> staffDetails;
}
