package com.example.ssa.entity.user;

import com.example.ssa.entity.user.constants.ManagerConstants;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * All information required for a manager within the application.
 * Most details are encompassed in AppUser entity.
 * @see AppUser for more information about the details of the user.
 */
@Entity
@Table(name= ManagerConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Manager  {
    @Id
    @Column(name = ManagerConstants.USER_ID)
    private Long id;

    @OneToOne()
    @MapsId
    @JoinColumn(name = ManagerConstants.USER_ID)
    private AppUser userDetails;

    @JoinTable(
        name = ManagerConstants.STAFF_DETAILS_TABLE_LINK,
        joinColumns=@JoinColumn(name = ManagerConstants.STAFF_DETAILS_JOIN_COLUMNS),
        inverseJoinColumns=@JoinColumn(name = ManagerConstants.STAFF_DETAILS_INVERSE_JOIN_COLUMNS)
    )
    @OneToMany()
    private List<AppUser> staffDetails;
}
