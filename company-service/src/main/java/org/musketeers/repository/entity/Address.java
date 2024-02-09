package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder // Volkan. @Builder kalmıştı @SuperBuilder yaptım, kod kızıyordu.
@Entity
@Table(name = "tbl_addresses")
public class Address extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String city;

    private String state;

    private String address;

    private String postalCode;
}
