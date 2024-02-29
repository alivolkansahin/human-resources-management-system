package org.musketeers.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequestMapping(FALLBACK)
public class FallbackController {

    @GetMapping(ADMIN)
    public ResponseEntity<String> fallbackAdmin(){
        return ResponseEntity.ok("Admin service is not responding currently. Please try again later...");
    }

    @GetMapping(ADVANCE)
    public ResponseEntity<String> fallbackAdvance(){
        return ResponseEntity.ok("Advance service is not responding currently. Please try again later...");
    }

    @GetMapping(AUTH)
    public ResponseEntity<String> fallbackAuth(){
        return ResponseEntity.ok("Auth service is not responding currently. Please try again later...");
    }

    @GetMapping(COMMENT)
    public ResponseEntity<String> fallbackComment(){
        return ResponseEntity.ok("Comment service is not responding currently. Please try again later...");
    }

    @GetMapping(COMPANY)
    public ResponseEntity<String> fallbackCompany(){
        return ResponseEntity.ok("Company service is not responding currently. Please try again later...");
    }

    @GetMapping(DAY_OFF)
    public ResponseEntity<String> fallbackDayOff(){
        return ResponseEntity.ok("Day Off service is not responding currently. Please try again later...");
    }

    @GetMapping(GUEST)
    public ResponseEntity<String> fallbackGuest(){
        return ResponseEntity.ok("Guest service is not responding currently. Please try again later...");
    }

    @GetMapping(MAIL)
    public ResponseEntity<String> fallbackMail(){
        return ResponseEntity.ok("Mail service is not responding currently. Please try again later...");
    }

    @GetMapping(PERSONNEL)
    public ResponseEntity<String> fallbackPersonnel(){
        return ResponseEntity.ok("Personnel service is not responding currently. Please try again later...");
    }

    @GetMapping(SPENDING)
    public ResponseEntity<String> fallbackSpending(){
        return ResponseEntity.ok("Spending service is not responding currently. Please try again later...");
    }

    @GetMapping(SUPERVISOR)
    public ResponseEntity<String> fallbackSupervisor(){
        return ResponseEntity.ok("Supervisor service is not responding currently. Please try again later...");
    }

}
