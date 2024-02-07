package org.musketeers.controller;


import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Supervisor;
import org.musketeers.service.SupervisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequestMapping(ROOT + SUPERVISOR )
@RequiredArgsConstructor
public class SupervisorController {

    private final SupervisorService supervisorService;

    @GetMapping(GET_ALL)
    public ResponseEntity<List<Supervisor>> getAllSupervisors(){
        return ResponseEntity.ok(supervisorService.getAllSupervisors());
    }

    @GetMapping(GET + "/{id}")
    public ResponseEntity<Supervisor> getSupervisorById(@PathVariable String id){
        return ResponseEntity.ok((supervisorService.getSupervisorById(id)));
    }

    @PostMapping(REGISTER)
    public ResponseEntity<Supervisor> register(@RequestBody Supervisor supervisor){
        return ResponseEntity.ok(supervisorService.register(supervisor));
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<Boolean> softDeleteSupervisorById(@PathVariable String id) {
        return ResponseEntity.ok(supervisorService.softDeleteSupervisorById(id));
    }

//    @PutMapping(UPDATE + "/{id}")
    @PutMapping(UPDATE)
    public ResponseEntity<Supervisor> updateSupervisorById(@RequestBody Supervisor supervisor){
        return ResponseEntity.ok(supervisorService.update(supervisor));
    }










}
