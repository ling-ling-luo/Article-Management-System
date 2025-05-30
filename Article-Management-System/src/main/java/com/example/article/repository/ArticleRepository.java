package com.example.article.repository;

import com.example.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    Page<Article> findByCategoryId(Long categoryId, Pageable pageable);
    
    List<Article> findByCategoryId(Long categoryId);
    
    Page<Article> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    
    Page<Article> findByTitleContainingAndCategoryId(String title, Long categoryId, Pageable pageable);
    
    Page<Article> findByTitleContaining(String title, Pageable pageable);
} 