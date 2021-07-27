package com.example.ssa.entity.user;

import com.example.ssa.entity.user.constants.ManagerConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= ManagerConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Manager  {
    @Id
    @Column(name = ManagerConstants.USER_ID)
    private long id;

    @OneToOne
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
