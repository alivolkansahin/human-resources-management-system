package org.musketeers.controller;


import org.musketeers.entity.Auth;
import org.musketeers.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.musketeers.constant.EndPoints.*;


@RestController
@RequestMapping(ROOT + AUTH)
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping(DENEME_REGISTER)
    public ResponseEntity<Auth> registerDeneme(@RequestBody Auth auth) {
        return ResponseEntity.ok(authService.registerDeneme(auth));
    }
}
