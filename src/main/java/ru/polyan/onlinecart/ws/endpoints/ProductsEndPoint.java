package ru.polyan.onlinecart.ws.endpoints;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.polyan.onlinecart.dto.ProductsRequestDTO;
import ru.polyan.onlinecart.exception.ResourceNotFoundException;
import ru.polyan.onlinecart.model.Product;
import ru.polyan.onlinecart.services.ProductService;
import ru.polyan.onlinecart.ws.soap.products.*;

import java.util.HashMap;
import java.util.function.Function;

@Endpoint
@RequiredArgsConstructor
public class ProductsEndPoint {

    private static final String NAMESPACE_URI = "http://www.polyan.ru/api/products";
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public static final Function<Product, SoapProduct> functionEntityToSoap = product -> {
        SoapProduct soapProduct = new SoapProduct();
        soapProduct.setTitle(product.getTitle());
        soapProduct.setPrice(product.getPrice());
        return soapProduct;
    };

    /*
        Пример запроса: POST http://localhost:8080/onlinecart/ws/products

        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:f="http://www.polyan.ru/api/products">
            <soapenv:Header/>
            <soapenv:Body>

                <f:getAllProductsRequest>
                    <f:pageNumber>1</f:pageNumber>
                    <f:pageSize>6</f:pageSize>
                </f:getAllProductsRequest>

            </soapenv:Body>
        </soapenv:Envelope>
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) {
        GetAllProductsResponse response = new GetAllProductsResponse();
        ProductsRequestDTO productsRequestDTO = modelMapper.map(request, ProductsRequestDTO.class);
        Page<Product> productPage;
        productPage = productService.findAll(productsRequestDTO);
        productPage.forEach(product-> response.getProduct().add(functionEntityToSoap.apply(product)));
        return response;
    }

/* Поиск по ИД
        Пример запроса: POST http://localhost:8080/onlinecart/ws/products

        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:f="http://www.polyan.ru/api/products">
            <soapenv:Header/>
            <soapenv:Body>

                <f:getProductRequest>
                    <f:id>1</f:id>
                </f:getProductRequest>

            </soapenv:Body>
        </soapenv:Envelope>
*/
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductRequest")
    @ResponsePayload
    public GetProductResponse getAllProduct(@RequestPayload GetProductRequest request) {
        GetProductResponse response = new GetProductResponse();
        Page<Product> productPage;
        response.setProduct(functionEntityToSoap.apply(productService.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("Exception: product id=" + request.getId() + " not found."))));
        return response;
    }

}
