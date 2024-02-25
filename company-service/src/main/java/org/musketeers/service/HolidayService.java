package org.musketeers.service;

import org.musketeers.dto.request.AddHolidayRequestDto;
import org.musketeers.exception.CompanyServiceException;
import org.musketeers.exception.ErrorType;
import org.musketeers.repository.HolidayRepository;
import org.musketeers.repository.entity.Company;
import org.musketeers.repository.entity.Holiday;
import org.springframework.stereotype.Service;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    private final CompanyService companyService;

    public HolidayService(HolidayRepository holidayRepository, CompanyService companyService) {
        this.holidayRepository = holidayRepository;
        this.companyService = companyService;
    }

    public Boolean saveHoliday(AddHolidayRequestDto dto){
        Company company = companyService.findById(dto.getCompanyId()).orElseThrow(() -> new CompanyServiceException(ErrorType.COMPANY_NOT_FOUND));
        Holiday holiday = Holiday.builder()
                .company(company)
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
        holidayRepository.save(holiday);
        return true;
    }
}
