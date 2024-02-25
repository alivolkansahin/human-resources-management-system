package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.request.AdvanceCancelRequestDto;
import org.musketeers.dto.request.AdvanceCreateRequestDto;
import org.musketeers.dto.request.AdvanceUpdateRequestDto;
import org.musketeers.dto.response.AdvanceGetAllMyRequestsResponseDto;
import org.musketeers.dto.response.AdvanceGetAllRequestsResponseDto;
import org.musketeers.service.AdvanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT + ADVANCE)
public class AdvanceController {

    private final AdvanceService advanceService;

    @PostMapping(CREATE_REQUEST)
    public ResponseEntity<String> createRequest(@RequestBody AdvanceCreateRequestDto dto) {
        return ResponseEntity.ok(advanceService.createRequest(dto));
    }

    @PatchMapping(CANCEL_REQUEST)
    public ResponseEntity<String> cancelRequest(@RequestBody AdvanceCancelRequestDto dto) {
        return ResponseEntity.ok(advanceService.cancelRequest(dto));
    }

    @PatchMapping(UPDATE_REQUEST)
    public ResponseEntity<String> updateRequestStatus(@RequestBody AdvanceUpdateRequestDto dto) {
        return ResponseEntity.ok(advanceService.updateRequestStatus(dto));
    }

    @GetMapping(GET_ALL_REQUESTS+"/{token}")
    public ResponseEntity<List<AdvanceGetAllRequestsResponseDto>> getAllRequestsForSupervisor(@PathVariable String token) {
        return ResponseEntity.ok(advanceService.getAllRequestsForSupervisor(token));
    }

    @GetMapping(GET_ALL_MY_REQUESTS+"/{token}")
    public ResponseEntity<List<AdvanceGetAllMyRequestsResponseDto>> getAllMyRequestsForPersonnel(@PathVariable String token) {
        return ResponseEntity.ok(advanceService.getAllMyRequestsForPersonnel(token));
    }

}
