package ru.polyan.onlinecart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.repositories.CategoryRepository;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> findById(Long id) throws ResourceNotFoundException {
        Optional<Category> cat = categoryRepository.findById(id);
        return cat;
    }

    public List<Category> findAll(){
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }
}
