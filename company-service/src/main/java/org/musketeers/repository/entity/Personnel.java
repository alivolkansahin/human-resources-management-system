package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tbl_personnel")
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String personnelId;

    @ManyToOne
    private Department department;

}
