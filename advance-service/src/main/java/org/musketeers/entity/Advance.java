package org.musketeers.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.musketeers.entity.enums.ERequestStatus;
import org.musketeers.entity.enums.EReason;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder
@Table(name = "tbl_advances")
public class Advance extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String personnelId;

    private String companyId;

    @Enumerated(EnumType.STRING)
    private EReason reason;

    private String description;

    private Double amount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ERequestStatus requestStatus = ERequestStatus.PENDING;

}
