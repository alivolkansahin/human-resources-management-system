package org.musketeers.service;

import lombok.RequiredArgsConstructor;
import org.musketeers.repository.HolidayRepository;
import org.musketeers.repository.entity.Holiday;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolidayService{
    private final HolidayRepository holidayRepository;

    public Boolean  saveHoliday(Holiday holiday){
        holidayRepository.save(holiday);
        return true;
    }
}
