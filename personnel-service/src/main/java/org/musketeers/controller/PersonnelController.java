package org.musketeers.controller;


import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Personnel;
import org.musketeers.service.PersonnelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequestMapping(ROOT + PERSONNEL )
@RequiredArgsConstructor
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

    //rabbitmq artık...
//    @PostMapping(REGISTER)
//    public ResponseEntity<Personnel> register(@RequestBody Personnel personnel){
//        return ResponseEntity.ok(personnelService.register(personnel));
//    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<Boolean> softDeletePersonnelById(@PathVariable String id) {
        return ResponseEntity.ok(personnelService.softDeletePersonnelById(id));
    }

//    @PutMapping(UPDATE + "/{id}")
    @PutMapping(UPDATE)
    public ResponseEntity<Personnel> updatePersonnelById(@RequestBody Personnel personnel){
        return ResponseEntity.ok(personnelService.update(personnel));
    }










}
