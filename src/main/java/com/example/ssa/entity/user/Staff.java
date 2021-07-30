package com.example.ssa.entity.user;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.constants.StaffConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= StaffConstants.TABLE_NAME)
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

    @OneToMany(mappedBy = "staff")
    private List<StaffSkill> skills = new ArrayList<>();
}
