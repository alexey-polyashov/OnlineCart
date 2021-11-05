package ru.polyan.onlinecart.controllers.products;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polyan.onlinecart.dto.ProductDto;
import ru.polyan.onlinecart.dto.ProductsRequestDTO;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.services.CategoryService;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products/product/")
@Slf4j
@Api(value = "product", tags = "Контролер товаров", description = "Управляет товарами")
public class ProductsApiV1 {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping(value = "")
    //@PostMapping(value = "")
    @ResponseBody
    @ApiImplicitParams(value={
            @ApiImplicitParam(name="miprice", value = "Минимальная цена", required = false),
            @ApiImplicitParam(name="maxprice", value = "Максимальная цена", required = false),
            @ApiImplicitParam(name="title", value = "Фильтр по названию товара", required = false),
            @ApiImplicitParam(name="category", value = "ID выбранной категории", required = false),
            @ApiImplicitParam(name="page", value = "Номер страницы", required = false, defaultValue = "0"),
            @ApiImplicitParam(name="recordsOnPage", value = "Минимальная цена", required = false, defaultValue = "5")
    })
    public Page<ProductDto> findProducts(@RequestParam(name = "minprice", required = false, defaultValue = "-1") BigDecimal minprice,
                                         @RequestParam(name = "maxprice", required = false, defaultValue = "-1") BigDecimal maxprice,
                                         @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                         @RequestParam(name = "category", required = false, defaultValue = "-1") Long category,
                                         @RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "5") int recordsOnPage){
        Page<ProductDto> productDtoPage;
        Page<Product> productPage;

        ProductsRequestDTO productsRequestDTO = new ProductsRequestDTO();
        productsRequestDTO.setMinprice(minprice);
        productsRequestDTO.setMaxprice(maxprice);
        productsRequestDTO.setTitle(title);
        productsRequestDTO.setCategoryId(category);
        productsRequestDTO.setPage(page);
        productsRequestDTO.setRecordsOnPage(recordsOnPage);

        productPage = productService.findAll(productsRequestDTO);
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

    @GetMapping(value = "/{id}/getimage/{size}")
    @ResponseBody
    public ResponseEntity<byte[]> getImageByID(@PathVariable Long id, @PathVariable String size) {
        Path imgPath = productService.getImageFile(id, size);
        try {
            return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE)).body(Files.readAllBytes(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("File not found");
        }
    }




}
