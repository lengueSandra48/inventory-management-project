package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.AuthResponseDto;
import com.team48.gestiondestock.dto.LoginRequestDto;
import com.team48.gestiondestock.dto.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);

}
