package org.musketeers.controller;


import lombok.RequiredArgsConstructor;
import org.musketeers.entity.Guest;
import org.musketeers.service.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequestMapping(ROOT + GUEST )
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping(GET_ALL)
    public ResponseEntity<List<Guest>> getAllGuests(String token){
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @GetMapping(GET + "/{id}") // dtosu sonra yapılacak.
    public ResponseEntity<Guest> getGuestById(@PathVariable String id){
        return ResponseEntity.ok((guestService.getGuestById(id)));
    }

//    @PostMapping(REGISTER)
//    public ResponseEntity<Guest> register(@RequestBody Guest guest){
//        return ResponseEntity.ok(guestService.register(guest));
//    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<Boolean> softDeleteGuestById(@PathVariable String id) {
        return ResponseEntity.ok(guestService.softDeleteGuestById(id));
    }

//    @PutMapping(UPDATE + "/{id}")
    @PutMapping(UPDATE)
    public ResponseEntity<Guest> updateGuestById(@RequestBody Guest guest){
        return ResponseEntity.ok(guestService.update(guest));
    }










}
