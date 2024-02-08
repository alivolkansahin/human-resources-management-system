package org.musketeers.service;

import org.musketeers.repository.HRInfoRepository;
import org.musketeers.repository.entity.HRInfo;
import org.musketeers.utility.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class HRInfoService extends ServiceManager<HRInfo, Long> {
    private final HRInfoRepository hrInfoRepository;

    public HRInfoService(HRInfoRepository hrInfoRepository) {
        super(hrInfoRepository);
        this.hrInfoRepository = hrInfoRepository;
    }

    public ResponseEntity<Boolean> saveHRInfo(HRInfo hrInfo){
        save(hrInfo);
        return ResponseEntity.ok(true);
    }
}
