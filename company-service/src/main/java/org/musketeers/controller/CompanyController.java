package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.request.AddDepartmentRequestDto;
import org.musketeers.dto.request.AddHolidayRequestDto;
import org.musketeers.dto.request.AddIncomeRequestDto;
import org.musketeers.dto.request.CompanyUpdateRequestDTO;
import org.musketeers.dto.response.GetCompanyDetailedInfoResponseDto;
import org.musketeers.dto.response.GetCompanySummaryInfoResponseDto;
import org.musketeers.repository.entity.Company;
import org.musketeers.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.EndPoints.*;

@RestController
@RequestMapping(ROOT+COMPANY)
@RequiredArgsConstructor
@CrossOrigin
public class CompanyController {

    private final CompanyService companyService;
    private final DepartmentService departmentService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final HolidayService holidayService;

    @GetMapping(FINDALL)
    public List<Company> findAll(){
        return companyService.findAll();
    }

    @GetMapping(FINDCOMPANYBYSUPERVISORTOKEN + "/{token}")
    public Company findByCompanyId(@PathVariable String token){
        return companyService.findByCompanyId(token);
    }

    @PutMapping(UPDATE_FOR_THE_FIRST_TIME)
    public ResponseEntity<Boolean> updateCompanyForFirstTime(@RequestBody CompanyUpdateRequestDTO dto){
        return ResponseEntity.ok(companyService.updateCompanyForFirstTime(dto));
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
    public ResponseEntity<Boolean> addIncome(@RequestBody AddIncomeRequestDto dto){
        return ResponseEntity.ok(incomeService.saveIncome(dto));
    }

//    @PostMapping(ADD_EXPENSE)
//    public ResponseEntity<Boolean> addExpense(@RequestBody AddExpenseRequestDto dto){
//        return ResponseEntity.ok(expenseService.saveExpense(dto));
//    }

    @PostMapping(ADD_HOLIDAY)
    public ResponseEntity<Boolean> addHoliday(@RequestBody AddHolidayRequestDto dto){
        return ResponseEntity.ok(holidayService.saveHoliday(dto));
    }

//    @PostMapping(ADD_HRINFO)
//    public ResponseEntity<Boolean> addHRInfo(AddHolidayRequestDto dto){
//        return ResponseEntity.ok(holidayService.saveHoliday(companyMapper.addHolidayRequestDtoToHoliday(dto)));
//    }

    @PostMapping(ADD_DEPARTMENT)
    public ResponseEntity<Boolean> addDepartment(@RequestBody AddDepartmentRequestDto dto){
        return ResponseEntity.ok(departmentService.saveDepartment(dto));
    }

    @GetMapping(GET_COMPANY_SUMMARY_INFO_FOR_GUEST + "/{companyName}")
    public ResponseEntity<List<GetCompanySummaryInfoResponseDto>> getCompanySummaryInfo(@PathVariable String companyName){
        return ResponseEntity.ok(companyService.getCompanySummaryInfo(companyName));
    }

    @GetMapping(GET_COMPANY_DETAILED_INFO_FOR_GUEST + "/{companyId}")
    public ResponseEntity<GetCompanyDetailedInfoResponseDto> getCompanyDetailedInfo(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetailedInfoById(companyId));
    }

}
