package com.example.article.controller;

import com.example.article.dto.ApiResponse;
import com.example.article.dto.ArticleDTO;
import com.example.article.entity.Article;
import com.example.article.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ApiResponse<Article> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        Article createdArticle = articleService.createArticle(articleDTO);
        return ApiResponse.success("文章创建成功", createdArticle);
    }

    @PutMapping("/{id}")
    public ApiResponse<Article> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDTO articleDTO) {
        Article updatedArticle = articleService.updateArticle(id, articleDTO);
        return ApiResponse.success("文章更新成功", updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ApiResponse.success("文章删除成功", null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        return ApiResponse.success(article);
    }

    @GetMapping
    public ApiResponse<Page<Article>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("asc") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Article> articles = articleService.findArticles(title, categoryId, pageable);
        return ApiResponse.success(articles);
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<Page<Article>> getArticlesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Article> articles = articleService.getArticlesByCategory(categoryId, pageable);
        return ApiResponse.success(articles);
    }

    @GetMapping("/search")
    public ApiResponse<Page<Article>> searchArticles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Article> articles = articleService.searchArticles(keyword, pageable);
        return ApiResponse.success(articles);
    }
} 