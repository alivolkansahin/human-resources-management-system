package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import static org.musketeers.constant.EndPoints.*;

import org.musketeers.dto.request.*;
import org.musketeers.mapper.ICompanyMapper;
import org.musketeers.repository.entity.Company;
import org.musketeers.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ROOT+COMPANY)
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final DepartmentService departmentService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final HolidayService holidayService;
    private final ICompanyMapper companyMapper;

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
    public ResponseEntity<Boolean> updateCompany(@RequestBody CompanyUpdateRequestDTO dto){
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

    @PostMapping(ADD_INCOME)
    public ResponseEntity<Boolean> addIncome(AddIncomeRequestDto dto){
        return ResponseEntity.ok(incomeService.saveIncome(companyMapper.addIncomeRequestDtoToIncome(dto)));
    }

    @PostMapping(ADD_EXPENSE)
    public ResponseEntity<Boolean> addExpense(AddExpenseRequestDto dto){
        return ResponseEntity.ok(expenseService.saveExpense(companyMapper.addExpenseRequestDtoToExpense(dto)));
    }

    @PostMapping(ADD_HOLIDAY)
    public ResponseEntity<Boolean> addHoliday(AddHolidayRequestDto dto){
        return ResponseEntity.ok(holidayService.saveHoliday(companyMapper.addHolidayRequestDtoToHoliday(dto)));
    }

    @PostMapping(ADD_HRINFO)
    public ResponseEntity<Boolean> addHRInfo(AddHolidayRequestDto dto){
        return ResponseEntity.ok(holidayService.saveHoliday(companyMapper.addHolidayRequestDtoToHoliday(dto)));
    }

    @PostMapping(ADD_DEPARTMENT)
    public ResponseEntity<Boolean> addDepartment(AddDepartmentRequestDto dto){
        return ResponseEntity.ok(departmentService.saveDepartment(companyMapper.addDepartmentRequestDtoToDepartment(dto)));
    }
}
