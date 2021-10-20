package ru.polyan.onlinecart.services;

import lombok.RequiredArgsConstructor;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryList productRepository;

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> findAll(int page, int recordsOrPage, Map<String, String> params) {
        List<String> errors = new ArrayList<>();
        Specification<Product> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if(entry.getKey()=="minprice"){
                try {
                    BigDecimal val = NumberUtils.parseNumber(entry.getValue(), BigDecimal.class);
                    if (val.compareTo(new BigDecimal(0)) == 1) {
                        spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThan(val));
                    }
                }catch (IllegalArgumentException e){
                    errors.add("Минимальная цена содержит не корректное число");
                }
            }
            if(entry.getKey()=="maxprice"){
                try {
                    BigDecimal val = NumberUtils.parseNumber(entry.getValue(), BigDecimal.class);
                    if (val.compareTo(new BigDecimal(0)) == 1) {
                        spec = spec.and(ProductSpecifications.priceLessOrEqualsThan(val));
                    }
                }catch (IllegalArgumentException e){
                    errors.add("Максимальная цена содержит не корректное число");
                }
            }
            if(entry.getKey()=="title"){
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
