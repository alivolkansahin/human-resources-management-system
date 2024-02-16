package org.musketeers.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "tbl_departments")
public class Department extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JsonIgnore
    private Company company;

    private String name;

    private String shifts;

    private String breaks;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @Builder.Default
    private List<Personnel> personnel = new ArrayList<>();

}
