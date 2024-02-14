package org.musketeers.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Data
@SuperBuilder
public class BaseEntity {

    private Long createdAt;

    private Long updatedAt;

    private Boolean status;

}
