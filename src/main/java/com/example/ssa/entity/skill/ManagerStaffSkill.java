package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.ManagerStaffSkillConstants;
import com.example.ssa.entity.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * All information required for a manager staff skill.
 * This entity is used for obtaining a list of staff who have assigned themselves to the skill.
 * @see StaffSkill for the information on how staff assign themselves to a skill.
 */
@Entity
@Table(name = ManagerStaffSkillConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
