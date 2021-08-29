package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.StaffSkillConstants;
import com.example.ssa.entity.user.AppUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * All information required for a staff skill.
 * This entity is used as a way to represent a staffs rating of the specific skill out of their skill set.
 */
@Entity
@Table(name = StaffSkillConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StaffSkill {
    @Id
    @Column(name = StaffSkillConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = StaffSkillConstants.SKILL_ID)
    private Skill skill;

    @ManyToOne()
    @JoinColumn(name = StaffSkillConstants.STAFF_ID)
    private AppUser staffDetails;

    @Column(name = StaffSkillConstants.RATING)
    private Integer rating;

    /**
     * @apiNote Not currently used for any purpose.
     */
    @Column(name = StaffSkillConstants.LAST_UPDATED)
    private LocalDateTime lastUpdated;

    @Column(name = StaffSkillConstants.EXPIRES)
    private LocalDateTime expires;
}
