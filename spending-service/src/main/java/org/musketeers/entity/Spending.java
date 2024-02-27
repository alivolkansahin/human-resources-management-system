package org.musketeers.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.musketeers.entity.enums.ECurrency;
import org.musketeers.entity.enums.ERequestStatus;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder
@Table(name = "tbl_spendings")
public class Spending extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String personnelId;

    private String companyId;

    private String description;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private ECurrency currency;

    private LocalDate spendingDate;

    @OneToMany(mappedBy = "spending", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Attachment> attachments;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ERequestStatus requestStatus = ERequestStatus.PENDING;

}
