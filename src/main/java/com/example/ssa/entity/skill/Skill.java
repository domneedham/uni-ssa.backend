package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.SkillConstants;
import lombok.*;

import javax.persistence.*;

/**
 * All information required for a skill.
 * This entity is used for representing a common skill that staff members may have.
 */
@Entity
@Table(name = SkillConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Skill {
    @Id
    @Column(name = SkillConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = SkillConstants.NAME)
    private String name;

    @ManyToOne()
    @JoinColumn(name = SkillConstants.CATEGORY_ID)
    private Category category;
}
