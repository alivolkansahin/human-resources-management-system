package org.musketeers.controller;


import lombok.RequiredArgsConstructor;
import org.musketeers.dto.request.CreatePersonnelRequestDto;
import org.musketeers.dto.request.GetPersonnelByCompanyRequestDto;
import org.musketeers.entity.Personnel;
import org.musketeers.service.PersonnelService;
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

    @GetMapping(GET + "/{id}")
    public ResponseEntity<Personnel> getPersonnelById(@PathVariable String id){
        return ResponseEntity.ok((personnelService.getPersonnelById(id)));
    }

    @PostMapping(CREATE)
    public ResponseEntity<String> createPersonnel(@RequestBody CreatePersonnelRequestDto dto){
        return ResponseEntity.ok(personnelService.createPersonnel(dto));
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<Boolean> softDeletePersonnelById(@PathVariable String id) {
        return ResponseEntity.ok(personnelService.softDeletePersonnelById(id));
    }

//    @PutMapping(UPDATE + "/{id}")
    @PutMapping(UPDATE)
    public ResponseEntity<Personnel> updatePersonnelById(@RequestBody Personnel personnel){
        return ResponseEntity.ok(personnelService.update(personnel));
    }

    @GetMapping(GET_ALL + "-by-company/{token}")
    public ResponseEntity<List<Personnel>> getAllByCompanyId(@PathVariable String token) {
        return ResponseEntity.ok(personnelService.getAllByCompanyId(token));
    }










}
