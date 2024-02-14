package org.musketeers.controller;


import jakarta.validation.Valid;
import org.musketeers.dto.request.LoginRequestDto;
import org.musketeers.dto.request.GuestRegisterRequestDto;
import org.musketeers.dto.request.SupervisorRegisterRequestDto;
import org.musketeers.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.musketeers.constant.EndPoints.*;


@RestController
@RequestMapping(ROOT + AUTH)
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(GUEST_REGISTER)
    public ResponseEntity<String> registerGuest(@Valid @RequestBody GuestRegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerGuest(dto));
    }

    @PostMapping(SUPERVISOR_REGISTER)
    public ResponseEntity<String> registerSupervisor(@Valid @RequestBody SupervisorRegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerSupervisor(dto));
    }

    @GetMapping(ACTIVATE_GUEST+"/{id}")
    public ResponseEntity<String> activateGuest(@PathVariable String id){
        return ResponseEntity.ok(authService.activateGuest(id));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }
}
