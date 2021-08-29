package com.example.ssa.entity.skill;

import com.example.ssa.entity.skill.constants.CategoryConstants;
import lombok.*;

import javax.persistence.*;

/**
 * All information required for a category.
 * This entity is used for grouping skills into a common form.
 */
@Entity
@Table(name = CategoryConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    /**
     * Stored as an integer as this is the IconCode in Flutter.
     */
    @Column(name = CategoryConstants.ICON)
    private Integer icon;
}
