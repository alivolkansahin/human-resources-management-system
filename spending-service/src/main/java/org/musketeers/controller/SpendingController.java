package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import org.musketeers.service.SpendingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT + SPENDING)
@CrossOrigin
public class SpendingController {

    private final SpendingService spendingService;

    @PostMapping(CREATE_REQUEST)
    public ResponseEntity<String> createRequest(@RequestBody DayOffCreateRequestDto dto) {
        return ResponseEntity.ok(spendingService.createRequest(dto));
    }

    @PatchMapping(CANCEL_REQUEST)
    public ResponseEntity<String> cancelRequest(@RequestBody DayOffCancelRequestDto dto) {
        return ResponseEntity.ok(spendingService.cancelRequest(dto));
    }

    @PatchMapping(UPDATE_REQUEST)
    public ResponseEntity<String> updateRequestStatus(@RequestBody DayOffUpdateRequestDto dto) {
        return ResponseEntity.ok(spendingService.updateRequestStatus(dto));
    }

    @GetMapping(GET_ALL_REQUESTS+"/{token}")
    public ResponseEntity<List<DayOffGetAllRequestsResponseDto>> getAllRequestsForSupervisor(@PathVariable String token) {
        return ResponseEntity.ok(spendingService.getAllRequestsForSupervisor(token));
    }

    @GetMapping(GET_ALL_MY_REQUESTS+"/{token}")
    public ResponseEntity<List<DayOffGetAllMyRequestsResponseDto>> getAllMyRequestsForPersonnel(@PathVariable String token) {
        return ResponseEntity.ok(spendingService.getAllMyRequestsForPersonnel(token));
    }

}
