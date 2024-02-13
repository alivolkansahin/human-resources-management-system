package org.musketeers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.musketeers.dto.request.AddDepartmentRequestDto;
import org.musketeers.dto.request.AddExpenseRequestDto;
import org.musketeers.dto.request.AddHolidayRequestDto;
import org.musketeers.dto.request.AddIncomeRequestDto;
import org.musketeers.repository.entity.Department;
import org.musketeers.repository.entity.Expense;
import org.musketeers.repository.entity.Holiday;
import org.musketeers.repository.entity.Income;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICompanyMapper {
    ICompanyMapper INSTANCE = Mappers.getMapper(ICompanyMapper.class);

    Income addIncomeRequestDtoToIncome (AddIncomeRequestDto dto);

    Expense addExpenseRequestDtoToExpense(AddExpenseRequestDto dto);

    Holiday addHolidayRequestDtoToHoliday(AddHolidayRequestDto dto);

    Department addDepartmentRequestDtoToDepartment(AddDepartmentRequestDto dto);
}
