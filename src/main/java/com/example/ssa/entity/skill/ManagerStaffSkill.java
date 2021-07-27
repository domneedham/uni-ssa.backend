package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.ManagerStaffSkillConstants;
import com.example.ssa.entity.user.AppUser;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne()
    @JoinColumn(name = ManagerStaffSkillConstants.SKILL_ID)
    private Skill skill;

    @OneToOne()
    @JoinColumn(name = ManagerStaffSkillConstants.STAFF_ID)
    private AppUser staffDetails;
}
