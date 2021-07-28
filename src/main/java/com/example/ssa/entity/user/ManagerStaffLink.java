package com.example.ssa.entity.user;

import com.example.ssa.entity.user.constants.ManagerStaffLinkConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name= ManagerStaffLinkConstants.TABLE_NAME)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ManagerStaffLink {
    @Id
    @Column(name = ManagerStaffLinkConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = ManagerStaffLinkConstants.MANAGER_ID)
    private long managerId;

    @Column(name = ManagerStaffLinkConstants.STAFF_ID)
    private long staffId;
}
