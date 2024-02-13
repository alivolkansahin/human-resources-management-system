package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tbl_supervisors")
public class Supervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String supervisorId;

    @ManyToOne
    private Company company;

}
