package org.musketeers.service;

import org.musketeers.repository.HolidayRepository;
import org.musketeers.repository.entity.Holiday;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class HolidayService extends ServiceManager<Holiday, Long> {
    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        super(holidayRepository);
        this.holidayRepository = holidayRepository;
    }

    public ResponseEntity<Boolean>  saveHoliday(Holiday holiday){
        save(holiday);
        return ResponseEntity.ok(true);
    }
}
