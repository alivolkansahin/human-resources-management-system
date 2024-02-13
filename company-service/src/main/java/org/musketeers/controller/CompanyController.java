package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import static org.musketeers.constant.EndPoints.*;

import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.repository.entity.Company;
import org.musketeers.service.CompanyService;
import org.musketeers.service.IncomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ROOT+COMPANY)
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final IncomeService incomeService;

    @PostMapping(SAVE)
    public boolean save(@RequestBody  Company company) {
        return companyService.createCompany(company);
    }

    @GetMapping(FINDALL)
    public List<Company> findAll(){
        return companyService.findAll();
    }

    @GetMapping(FINDCOMPANYBYSUPERVISORTOKEN)
    public Company findByCompanyId(String supervisorToken){
        return companyService.findByCompanyId(supervisorToken);
    }

    @PostMapping(UPDATE)
    public ResponseEntity<Boolean> updateProfile(@RequestBody CompanyUpdateRequestDTO dto){
        return ResponseEntity.ok(companyService.updateCompany(dto));
    }

    @PostMapping(SOFT_DELETE)
    public ResponseEntity<Boolean> softDelete(String companyName){
        return ResponseEntity.ok(companyService.softDelete(companyName));
    }

    @PostMapping(HARD_DELETE)
    public ResponseEntity<Boolean> hardDelete(String companyName){
        return ResponseEntity.ok(companyService.hardDelete(companyName));
    }

//    @PostMapping(ADD_INCOME)
//    public ResponseEntity<Boolean> addIncome(){
//        return ResponseEntity.ok(incomeService.saveIncome());
//    }
}
