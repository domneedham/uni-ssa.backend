package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.CategoryConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = CategoryConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Category {
    @Id
    @Column(name = CategoryConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = CategoryConstants.NAME)
    private String name;

    @Column(name = CategoryConstants.ICON)
    private Integer icon;
}
