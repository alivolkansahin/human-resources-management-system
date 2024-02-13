package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "tbl_departments")
public class Department extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String companyId;
    private String name;
    private String shifts;
    private String breaks;

    @OneToMany(mappedBy = "employeeId",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<EmployeeId> employeeIds = new ArrayList<>();
}
