package ru.polyan.onlinecart.controllers.products;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.CategoryDto;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.services.CategoryService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products/category/")
@Slf4j
public class CategoryApiV1 {

    private final CategoryService categoryService;

    @GetMapping(value="{id}")
    @ResponseBody
    public ResponseEntity<?> geCategoryById(@PathVariable long id) {
       Category category = categoryService.findById(id).orElseThrow(()->new ResourceNotFoundException("Exception: category id=" + id + " not found."));
       return new ResponseEntity<>(new CategoryDto(category), HttpStatus.OK);
    }

    @GetMapping(value="/")
    @ResponseBody
    public ResponseEntity<?> geCategories(){
        try{
            List<Category> categories = categoryService.findAll();
            List<CategoryDto> categoryDtos = categories.stream().map(c->new CategoryDto(c)).collect(Collectors.toList());
            categoryDtos.sort(null);
            return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
