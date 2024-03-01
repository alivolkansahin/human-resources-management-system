package org.musketeers.controller;


import lombok.RequiredArgsConstructor;
import org.musketeers.dto.request.CreatePersonnelRequestDto;
import org.musketeers.dto.request.UpdatePersonnelRequestDto;
import org.musketeers.dto.response.GetPersonnelDetailsResponseDto;
import org.musketeers.entity.Personnel;
import org.musketeers.service.PersonnelService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequestMapping(ROOT + PERSONNEL )
@RequiredArgsConstructor
@CrossOrigin
public class PersonnelController {

    private final PersonnelService personnelService;

    @GetMapping(GET_ALL)
    public ResponseEntity<List<Personnel>> getAllPersonnel(){
        return ResponseEntity.ok(personnelService.getAllPersonnel());
    }

    @GetMapping(GET + "/{token}")
    public ResponseEntity<GetPersonnelDetailsResponseDto> getPersonnelDetailsByToken(@PathVariable String token){
        return ResponseEntity.ok((personnelService.getPersonnelDetailsByToken(token)));
    }

    @PostMapping(CREATE)
    public ResponseEntity<String> createPersonnel(@RequestBody CreatePersonnelRequestDto dto){
        return ResponseEntity.ok(personnelService.createPersonnel(dto));
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<Boolean> softDeletePersonnelById(@PathVariable String id) {
        return ResponseEntity.ok(personnelService.softDeletePersonnelById(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updatePersonnelProfile(@RequestBody UpdatePersonnelRequestDto dto){
        return ResponseEntity.ok(personnelService.updatePersonnelProfile(dto));
    }

    @GetMapping(GET_ALL + "-by-company/{token}")
    public ResponseEntity<List<Personnel>> getAllByCompanyId(@PathVariable String token) {
        return ResponseEntity.ok(personnelService.getAllByCompanyId(token));
    }

}
