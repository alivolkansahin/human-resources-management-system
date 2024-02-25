package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.musketeers.repository.enums.ECurrency;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "tbl_contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double cost;

    @Enumerated(EnumType.STRING)
    private ECurrency currency;

}
