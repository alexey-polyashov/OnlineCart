package ru.polyan.onlinecart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import ru.polyan.onlinecart.dto.ProductDto;
import ru.polyan.onlinecart.exception.InvalidInputDataException;
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
public class ProductService {

    public static final String SMALL_IMAGE = "small";
    public static final String BIG_IMAGE = "big";



    private final ProductRepositoryList productRepository;

    private final static String FILTER_MIN_PRICE = "minprice";
    private final static String FILTER_MAX_PRICE = "maxprice";
    private final static String FILTER_TITLE = "title";

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> findAll(int page, int recordsOrPage, Map<String, String> params) {
        List<String> errors = new ArrayList<>();
        Specification<Product> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if(entry.getKey()==FILTER_MIN_PRICE && !entry.getValue().isBlank()){
                try {
                    BigDecimal val = NumberUtils.parseNumber(entry.getValue(), BigDecimal.class);
                    if (val.compareTo(new BigDecimal(0)) == 1) {
                        spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThan(val));
                    }
                }catch (IllegalArgumentException e){
                    errors.add("Минимальная цена должна содержать числом");
                }
            }
            if(entry.getKey()==FILTER_MAX_PRICE && !entry.getValue().isBlank()){
                try {
                    BigDecimal val = NumberUtils.parseNumber(entry.getValue(), BigDecimal.class);
                    if (val.compareTo(new BigDecimal(0)) == 1) {
                        spec = spec.and(ProductSpecifications.priceLessOrEqualsThan(val));
                    }
                }catch (IllegalArgumentException e){
                    errors.add("Максимальная цена должна содержать числом");
                }
            }
            if(entry.getKey()==FILTER_TITLE && !entry.getValue().isBlank()){
                String ft = entry.getValue();
                if(!ft.isBlank()){
                    spec = spec.and(ProductSpecifications.titleLike(entry.getValue()));
                }
            }
            if (!errors.isEmpty()) {
                throw new InvalidInputDataException(errors);
            }
        }
        return productRepository.findAll(spec, PageRequest.of(page, recordsOrPage));
    }

    public Optional<Product> getProductByID(Long id) {
        Optional<Product> prod = productRepository.findById(id);
        return prod;
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


}
