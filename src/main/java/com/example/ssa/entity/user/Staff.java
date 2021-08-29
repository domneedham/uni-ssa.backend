package com.example.ssa.entity.user;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.constants.StaffConstants;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All information required for a staff member within the application.
 * Most details are encompassed in AppUser entity.
 * @see AppUser for more information about the details of the user.
 */
@Entity
@Table(name= StaffConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Staff {
    @Id
    @Column(name = StaffConstants.USER_ID)
    private Long id;

    @OneToOne()
    @MapsId
    @JoinColumn(name = StaffConstants.USER_ID)
    private AppUser userDetails;

    @OneToOne()
    @JoinColumn(name = StaffConstants.MANAGER_ID)
    private AppUser managerDetails;

    @OneToMany(mappedBy = "staffDetails")
    private List<StaffSkill> skills = new ArrayList<>();
}
