package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.MvtStkRequestDto;
import com.team48.gestiondestock.dto.MvtStkResponseDto;

import java.util.List;

public interface MvtStkService {
    MvtStkResponseDto save(MvtStkRequestDto mvtStkDto);
    MvtStkResponseDto findById(Integer id);
    List<MvtStkResponseDto> findAll();
    void delete(Integer id);
    MvtStkResponseDto update(Integer id, MvtStkRequestDto mvtStkDto);
}
