package com.example.article.service.impl;

import com.example.article.dto.ArticleDTO;
import com.example.article.entity.Article;
import com.example.article.entity.Category;
import com.example.article.repository.ArticleRepository;
import com.example.article.repository.CategoryRepository;
import com.example.article.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Article createArticle(ArticleDTO articleDTO) {
        Category category = categoryRepository.findById(articleDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("分类不存在：ID = " + articleDTO.getCategoryId()));

        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setAuthor(articleDTO.getAuthor());
        article.setCategory(category);
        article.setStatus(articleDTO.getStatus() != null ? articleDTO.getStatus() : "draft");
        article.setPublishTime(articleDTO.getPublishTime() != null ? articleDTO.getPublishTime() : java.time.LocalDateTime.now());

        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public Article updateArticle(Long id, ArticleDTO articleDTO) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("文章不存在：ID = " + id));

        Category category = categoryRepository.findById(articleDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("分类不存在：ID = " + articleDTO.getCategoryId()));

        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setAuthor(articleDTO.getAuthor());
        article.setCategory(category);
        article.setStatus(articleDTO.getStatus() != null ? articleDTO.getStatus() : article.getStatus());
        article.setPublishTime(articleDTO.getPublishTime() != null ? articleDTO.getPublishTime() : article.getPublishTime());

        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new EntityNotFoundException("文章不存在：ID = " + id);
        }
        articleRepository.deleteById(id);
    }

    @Override
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("文章不存在：ID = " + id));
    }

    @Override
    public Page<Article> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Page<Article> getArticlesByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("分类不存在：ID = " + categoryId);
        }
        return articleRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Article> searchArticles(String keyword, Pageable pageable) {
        return articleRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    @Override
    public Page<Article> findArticles(String title, Long categoryId, Pageable pageable) {
        if (title != null && !title.isEmpty() && categoryId != null) {
            return articleRepository.findByTitleContainingAndCategoryId(title, categoryId, pageable);
        } else if (title != null && !title.isEmpty()) {
            return articleRepository.findByTitleContaining(title, pageable);
        } else if (categoryId != null) {
            return articleRepository.findByCategoryId(categoryId, pageable);
        } else {
            return articleRepository.findAll(pageable);
        }
    }
} 