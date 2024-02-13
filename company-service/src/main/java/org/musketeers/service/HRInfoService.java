package org.musketeers.service;

import org.musketeers.repository.HRInfoRepository;
import org.musketeers.repository.entity.HRInfo;
import org.musketeers.utility.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HRInfoService extends ServiceManager<HRInfo, String> {
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
