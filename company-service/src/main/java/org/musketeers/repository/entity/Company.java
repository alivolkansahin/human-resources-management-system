package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.repository.enums.EStatus;

import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private EStatus companyStatus = EStatus.PENDING;

    @OneToOne
    private Address address;
    // Volkan : addresi direkt string tutalım, diğer yerlerde öyle yapmıştık
//    private String address;

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

    // Volkan : bence buraya OneToMany ve Supervisor entity lazım, orada da id ve companyId tutulmalı. id supervisor servisinden gelcek, companyIdde burda tutuluyor.
    @ElementCollection
    private List<String> supervisorIds;
}
