package org.musketeers;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.musketeers.repository.CompanyRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Department;
import org.musketeers.repository.entity.HRInfo;
import org.musketeers.service.CompanyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;

@SpringBootApplication
public class CompanyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyServiceApplication.class);
    }
}