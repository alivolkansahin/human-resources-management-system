package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
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
