package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.SkillConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = SkillConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Skill {
    @Id
    @Column(name = SkillConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = SkillConstants.NAME)
    private String name;

    @ManyToOne()
    @JoinColumn(name = SkillConstants.CATEGORY_ID)
    private Category category;
}
