package com.team48.gestiondestock.controller.auth;

import com.team48.gestiondestock.dto.AuthResponseDto;
import com.team48.gestiondestock.dto.LoginRequestDto;
import com.team48.gestiondestock.dto.RegisterRequestDto;
import com.team48.gestiondestock.service.AuthService;
import com.team48.gestiondestock.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.APP_ROOT + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}