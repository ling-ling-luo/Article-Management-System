package com.example.article.service;

import com.example.article.dto.ArticleDTO;
import com.example.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    
    Article createArticle(ArticleDTO articleDTO);
    
    Article updateArticle(Long id, ArticleDTO articleDTO);
    
    void deleteArticle(Long id);
    
    Article getArticleById(Long id);
    
    Page<Article> getAllArticles(Pageable pageable);
    
    Page<Article> getArticlesByCategory(Long categoryId, Pageable pageable);
    
    Page<Article> searchArticles(String keyword, Pageable pageable);
    
    Page<Article> findArticles(String title, Long categoryId, Pageable pageable);
} 