package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.request.SpendingCancelRequestDto;
import org.musketeers.dto.request.SpendingCreateRequestDto;
import org.musketeers.dto.request.SpendingUpdateRequestDto;
import org.musketeers.dto.response.SpendingGetAllMyRequestsResponseDto;
import org.musketeers.dto.response.SpendingGetAllRequestsResponseDto;
import org.musketeers.service.SpendingService;
import org.springframework.http.MediaType;
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
    public ResponseEntity<String> createRequest(@RequestBody SpendingCreateRequestDto dto) {
        return ResponseEntity.ok(spendingService.createRequest(dto));
    }

    @PatchMapping(CANCEL_REQUEST)
    public ResponseEntity<String> cancelRequest(@RequestBody SpendingCancelRequestDto dto) {
        return ResponseEntity.ok(spendingService.cancelRequest(dto));
    }

    @PatchMapping(UPDATE_REQUEST)
    public ResponseEntity<String> updateRequestStatus(@RequestBody SpendingUpdateRequestDto dto) {
        return ResponseEntity.ok(spendingService.updateRequestStatus(dto));
    }

    @GetMapping(GET_ALL_REQUESTS+"/{token}")
    public ResponseEntity<List<SpendingGetAllRequestsResponseDto>> getAllRequestsForSupervisor(@PathVariable String token) {
        return ResponseEntity.ok(spendingService.getAllRequestsForSupervisor(token));
    }

    @GetMapping(GET_ALL_MY_REQUESTS+"/{token}")
    public ResponseEntity<List<SpendingGetAllMyRequestsResponseDto>> getAllMyRequestsForPersonnel(@PathVariable String token) {
        return ResponseEntity.ok(spendingService.getAllMyRequestsForPersonnel(token));
    }

}
