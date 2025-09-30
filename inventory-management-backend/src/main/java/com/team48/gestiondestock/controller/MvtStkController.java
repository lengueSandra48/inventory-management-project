package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.MvtStkApi;
import com.team48.gestiondestock.dto.MvtStkRequestDto;
import com.team48.gestiondestock.dto.MvtStkResponseDto;
import com.team48.gestiondestock.service.MvtStkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MvtStkController implements MvtStkApi {

    private final MvtStkService mvtStkService;

    @Autowired
    public MvtStkController(MvtStkService mvtStkService) {
        this.mvtStkService = mvtStkService;
    }

    @Override
    public ResponseEntity<MvtStkResponseDto> save(@RequestBody MvtStkRequestDto mvtStkDto) {
        MvtStkResponseDto savedMvt = mvtStkService.save(mvtStkDto);
        return ResponseEntity.ok(savedMvt);
    }

    @Override
    public MvtStkResponseDto findById(@PathVariable("idMvtStk") Integer id) {
        return mvtStkService.findById(id);
    }

    @Override
    public List<MvtStkResponseDto> findAll() {
        return mvtStkService.findAll();
    }

    @Override
    public void delete(@PathVariable("idMvtStk") Integer id) {
        mvtStkService.delete(id);
    }

    @Override
    public ResponseEntity<MvtStkResponseDto> update(@PathVariable("idMvtStk") Integer idMvtStk, @RequestBody MvtStkRequestDto mvtStkDto) {
        MvtStkResponseDto updatedMvt = mvtStkService.update(idMvtStk, mvtStkDto);
        return ResponseEntity.ok(updatedMvt);
    }
}
