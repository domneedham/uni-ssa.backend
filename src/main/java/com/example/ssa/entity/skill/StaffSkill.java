package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.StaffSkillConstants;
import com.example.ssa.entity.user.AppUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = StaffSkillConstants.LAST_UPDATED)
    private LocalDateTime lastUpdated;

    @Column(name = StaffSkillConstants.EXPIRES)
    private LocalDateTime expires;
}
