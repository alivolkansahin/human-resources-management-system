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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String companyId;
    private String name;

    // Supervisor id gibi buraya da Personnel entitysi lazım içerisinde id ve companyId olacak sadece. OneToMany ilişki - mappedBy
    @ElementCollection
    private List<String> employeeIds = new ArrayList<String>();

    // Aşağıdaki bütün hepsine bakılmalı, pozisyonları birden fazla vereceksek Entity Position, içerisine de sadece name alınabilir. One To Many olabilir
    // shifts ve breaks çalışma saatleri ise String tutulabilir private String shift = "9-12&13-17" falan. Frontta bunu ayarlarız sonuçta substring ile vs.
    @ElementCollection
    private Map<String,String> shift = new HashMap<String,String>();
    @ElementCollection
    private Map<Integer,String> breaks = new HashMap<Integer,String>();

    // Ya pozisyonlara aynı bu shift break gibi String position = "YAZILIM-INSANKAYNAKLARI-TEMIZLIKCI-RESEARCHER" falan filan gibi stringlerle de koyabiliriz birleşik şekil
    // tamamen sana bağlı yani OneToMany bi entityde oluşturabilirsin, ama bence bu Map stringler sorguda sıkıntı çıkarıyor entity olsalar daha iyi olacaklar gibi
    @ElementCollection
    private Map<Integer,String> positions = new HashMap<Integer,String>();
}
