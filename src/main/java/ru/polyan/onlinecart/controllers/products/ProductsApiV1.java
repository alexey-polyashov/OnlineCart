package ru.polyan.onlinecart.controllers.products;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.ProductDto;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.services.CategoryService;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;

import javax.activation.FileTypeMap;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products/product/")
@Slf4j
public class ProductsApiV1 {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping(value = "")
    @ResponseBody
    public Page<ProductDto> findProducts(@RequestParam(name = "minprice", required = false, defaultValue = "-1") String minprice,
                                         @RequestParam(name = "maxprice", required = false, defaultValue = "-1") String maxprice,
                                         @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                         @RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "5") int recordsOnPage){
        Page<ProductDto> productDtoPage;
        Page<Product> productPage;
        Map<String, String> params = new HashMap<>();
        params.put("maxprice", maxprice);
        params.put("minprice", minprice);
        params.put("title", title);
        productPage = productService.findAll(page, recordsOnPage, params);
        productDtoPage = productPage.map(p->new ProductDto(p));
        return productDtoPage;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> getProductByID(@PathVariable Long id) {
        Product product = productService.getProductByID(id).orElseThrow(() -> new ResourceNotFoundException("Exception: product id=" + id + " not found."));
        return new ResponseEntity<>(new ProductDto(product), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteProductById(@PathVariable Long id){
        try{
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestParam String title, @RequestParam String price, @RequestParam String categoryId){

        BigDecimal parsePrice = new BigDecimal(0);
        Long parseCategoryId = new Long(0);

        List<String> errors = new ArrayList<>();

        try {
            parsePrice = new BigDecimal(price);
        }catch (NumberFormatException e){
            errors.add("PRICE is not number");
        }

        Category category = null;
        try {
            parseCategoryId = Long.valueOf(categoryId);
            category = categoryService.findById(parseCategoryId).orElseThrow(()->new ResourceNotFoundException(""));
        }catch (NumberFormatException e){
            errors.add("CATEGORY is not number");
        }catch (ResourceNotFoundException e){
            errors.add("CATEGORY id= " + categoryId + " not found");
        }
        if(title.isEmpty()){
            errors.add("TITLE is empty");
        }

        ProductDto productDto;
        if(errors.isEmpty()){
            try {
                productDto = productService.addProduct(title, parsePrice, category);
                return new ResponseEntity<>(productDto, HttpStatus.OK);
            }catch (Exception e){
                log.error(String.join("; ", e.getMessage()));
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }else{
            log.error(String.join("; ", errors));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

    }





}
