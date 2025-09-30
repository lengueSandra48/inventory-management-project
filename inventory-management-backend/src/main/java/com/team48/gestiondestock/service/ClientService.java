package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.ClientRequestDto;
import com.team48.gestiondestock.dto.ClientResponseDto;

import java.util.List;

public interface ClientService {

    ClientResponseDto save(ClientRequestDto clientDto);

    ClientResponseDto findById(Integer id);

    ClientResponseDto findByNomClient(String nom);

    List<ClientResponseDto> findAll();

    void delete(Integer id);

    ClientResponseDto update(Integer id, ClientRequestDto clientDto);
}
