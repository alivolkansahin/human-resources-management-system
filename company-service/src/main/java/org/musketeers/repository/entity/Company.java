package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.musketeers.repository.enums.EStatus;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "tbl_companies")
public class Company extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String companyName;

    private LocalDate establishmentDate;

    private String companyLogo;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus companyStatus = EStatus.PENDING;

    private String address;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<HRInfo> hrInfos;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Department> departments;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Holiday> holidays;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Income> incomes;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "company",fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Supervisor> supervisors;

    @OneToOne
    @Cascade(CascadeType.ALL)
    private Contract contract;

}
