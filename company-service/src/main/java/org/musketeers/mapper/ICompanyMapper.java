package org.musketeers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.musketeers.dto.response.HRInfoResponseDto;
import org.musketeers.dto.response.SupervisorInfoResponseDto;
import org.musketeers.rabbitmq.model.GetCompanySupervisorResponseModel;
import org.musketeers.repository.entity.HRInfo;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICompanyMapper {
    ICompanyMapper INSTANCE = Mappers.getMapper(ICompanyMapper.class);

    SupervisorInfoResponseDto supervisorModelToDto(GetCompanySupervisorResponseModel model);

    HRInfoResponseDto hrInfosToDto(HRInfo hrInfo);

//    Income addIncomeRequestDtoToIncome (AddIncomeRequestDto dto);

//    Expense addExpenseRequestDtoToExpense(AddExpenseRequestDto dto);

//    Holiday addHolidayRequestDtoToHoliday(AddHolidayRequestDto dto);

//    Department addDepartmentRequestDtoToDepartment(AddDepartmentRequestDto dto);
}
