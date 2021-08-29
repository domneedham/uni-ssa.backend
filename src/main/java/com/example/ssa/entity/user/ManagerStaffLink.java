package com.example.ssa.entity.user;

import com.example.ssa.entity.user.constants.ManagerStaffLinkConstants;
import lombok.*;

import javax.persistence.*;

/**
* @deprecated no longer used.
 */

@Entity
@Table(name= ManagerStaffLinkConstants.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ManagerStaffLink {
    @Id
    @Column(name = ManagerStaffLinkConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = ManagerStaffLinkConstants.MANAGER_ID)
    private Long managerId;

    @Column(name = ManagerStaffLinkConstants.STAFF_ID)
    private Long staffId;
}
