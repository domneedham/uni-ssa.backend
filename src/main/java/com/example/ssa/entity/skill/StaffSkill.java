package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.StaffSkillConstants;
import com.example.ssa.entity.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = StaffSkillConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
// ignore staff property as redundant
@JsonIgnoreProperties("staff")
public class StaffSkill {
    @Id
    @Column(name = StaffSkillConstants.ID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne()
    @JoinColumn(name = StaffSkillConstants.SKILL_ID)
    private Skill skill;

    @ManyToOne()
    @JoinColumn(name = StaffSkillConstants.STAFF_ID)
    private AppUser staff;

    @Column(name = StaffSkillConstants.RATING)
    private Integer rating;

    @Column(name = StaffSkillConstants.LAST_UPDATED)
    private ZonedDateTime lastUpdated;

    @Column(name = StaffSkillConstants.EXPIRES)
    private ZonedDateTime expires;
}
