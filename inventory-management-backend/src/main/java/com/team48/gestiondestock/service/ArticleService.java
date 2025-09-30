package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.ArticleRequestDto;
import com.team48.gestiondestock.dto.ArticleResponseDto;

import java.util.List;

public interface ArticleService {

    ArticleResponseDto save(ArticleRequestDto articleDto);

    ArticleResponseDto findById(Integer id);

    ArticleResponseDto findByCodeArticle(String codeArticle);

    List<ArticleResponseDto> findAll();

    void delete(Integer id);

    ArticleResponseDto update(Integer id, ArticleRequestDto articleDto);

}
