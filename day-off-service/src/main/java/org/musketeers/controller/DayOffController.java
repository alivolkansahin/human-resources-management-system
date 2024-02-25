package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.request.DayOffCancelRequestDto;
import org.musketeers.dto.request.DayOffCreateRequestDto;
import org.musketeers.dto.request.DayOffUpdateRequestDto;
import org.musketeers.dto.response.DayOffGetAllMyRequestsResponseDto;
import org.musketeers.dto.response.DayOffGetAllRequestsResponseDto;
import org.musketeers.entity.DayOff;
import org.musketeers.service.DayOffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT + DAY_OFF)
public class DayOffController {

    private final DayOffService dayOffService;

    @PostMapping(CREATE_REQUEST)
    public ResponseEntity<String> createRequest(@RequestBody DayOffCreateRequestDto dto) {
        return ResponseEntity.ok(dayOffService.createRequest(dto));
    }

    @PatchMapping(CANCEL_REQUEST)
    public ResponseEntity<String> cancelRequest(@RequestBody DayOffCancelRequestDto dto) {
        return ResponseEntity.ok(dayOffService.cancelRequest(dto));
    }

    @PatchMapping(UPDATE_REQUEST)
    public ResponseEntity<String> updateRequestStatus(@RequestBody DayOffUpdateRequestDto dto) {
        return ResponseEntity.ok(dayOffService.updateRequestStatus(dto));
    }

    @GetMapping(GET_ALL_REQUESTS+"/{token}")
    public ResponseEntity<List<DayOffGetAllRequestsResponseDto>> getAllRequestsForSupervisor(@PathVariable String token) {
        return ResponseEntity.ok(dayOffService.getAllRequestsForSupervisor(token));
    }

    @GetMapping(GET_ALL_MY_REQUESTS+"/{token}")
    public ResponseEntity<List<DayOffGetAllMyRequestsResponseDto>> getAllMyRequestsForPersonnel(@PathVariable String token) {
        return ResponseEntity.ok(dayOffService.getAllMyRequestsForPersonnel(token));
    }

}
