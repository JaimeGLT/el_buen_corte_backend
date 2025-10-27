package com.el_buen_corte.el_buen_corte.category;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
            .name(request.getName())
            .build();

        category = categoryRepository.save(category);
        return toResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(item -> toResponse(item)).toList();
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
            .id(category.getId().toString())
            .name(category.getName())
            .build();

    }

}
