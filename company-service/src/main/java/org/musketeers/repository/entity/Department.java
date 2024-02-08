package org.musketeers.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long companyId;
    private String name;
    @ElementCollection
    private List<String> employeeIds = new ArrayList<String>();
    @ElementCollection
    private Map<String,String> shift = new HashMap<String,String>();
    @ElementCollection
    private Map<Integer,String> breaks = new HashMap<Integer,String>();
    @ElementCollection
    private Map<Integer,String> positions = new HashMap<Integer,String>();
}
