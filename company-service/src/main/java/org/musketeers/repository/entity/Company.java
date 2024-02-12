package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.repository.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "tbl_companies")
public class Company extends BaseEntity{

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String companyName;

    private String establishmentDate;

    private String companyLogo;

    @Builder.Default
    private Status status = Status.PENDING;

    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "companyId",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<HRInfo> hrInfos = new ArrayList<HRInfo>();

    @OneToMany(mappedBy = "companyId",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Department> departments = new ArrayList<Department>();

    @OneToMany(mappedBy = "companyId",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Holiday> holidays = new ArrayList<Holiday>();

    @OneToMany(mappedBy = "companyId",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Income> incomes = new ArrayList<Income>();

    @OneToMany(mappedBy = "companyId",cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<Expense> expenses = new ArrayList<Expense>();

    @ElementCollection
    private List<String> supervisorIds;
}
