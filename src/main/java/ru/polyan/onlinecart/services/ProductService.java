package ru.polyan.onlinecart.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.polyan.onlinecart.dto.ProductDto;
import ru.polyan.onlinecart.dto.ProductsRequestDTO;
import ru.polyan.onlinecart.model.Category;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.repositories.ProductRepositoryList;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.repositories.specifications.ProductSpecifications;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final CategoryService categoryService;

    public static final String SMALL_IMAGE = "small";
    public static final String BIG_IMAGE = "big";
    @Value("${utils.products.filestorage}")
    private String fileStorage;
    private final ProductRepositoryList productRepository;

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> findAll(ProductsRequestDTO productsRequestDTO){
        Specification<Product> spec = Specification.where(null);
        if(productsRequestDTO.getMinprice().compareTo(BigDecimal.ZERO)==1){
            spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThan(productsRequestDTO.getMinprice()));
        }
        if(productsRequestDTO.getMaxprice().compareTo(BigDecimal.ZERO)==1){
            spec = spec.and(ProductSpecifications.priceLessOrEqualsThan(productsRequestDTO.getMaxprice()));
        }
        if(!productsRequestDTO.getTitle().trim().isEmpty()){
            spec = spec.and(ProductSpecifications.titleLike(productsRequestDTO.getTitle().toLowerCase()));
        }
        Long catId = productsRequestDTO.getCategoryId();
        if(catId>0){
            Category category = categoryService.findById(
                    catId
            ).orElseThrow(()->new ResourceNotFoundException("Exception: category id=" + catId + " not found."));
            spec = spec.and(ProductSpecifications.category(category));
        }
        return productRepository.findAll(spec, PageRequest.of(productsRequestDTO.getPage(), productsRequestDTO.getRecordsOnPage()));
    }

    public Optional<Product> getProductByID(Long id) {
        return productRepository.findById(id);
    }

    public ProductDto addProduct(String title, BigDecimal price, Category category)  {

        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        productRepository.save(product);

        return new ProductDto(product);
    }

    public void deleteProduct(Long id) throws ResourceNotFoundException{
        productRepository.deleteById(id);
    }

    public Path getImageFile(Long id, String size) {

        log.info("getImageFile: id - {}; size - {}", id, size);

        long interval = ((id / 500)+1) * 500;
        Path filePath = Paths.get(fileStorage, String.valueOf(interval), String.valueOf(id));
        switch (size){
            case SMALL_IMAGE:
                filePath = Paths.get(filePath.toString(), "small.jpg");
                break;
            case BIG_IMAGE:
                filePath = Paths.get(filePath.toString(), "big.jpg");
                break;
            default:
                filePath = Paths.get(fileStorage, "default", "small.jpg");
        }
        if (!Files.exists(filePath)) {
            filePath = Paths.get(fileStorage,"default", "small.jpg");
        }
        log.info("getImageFile: filePath - {}", filePath);

        return filePath;

    }
}
