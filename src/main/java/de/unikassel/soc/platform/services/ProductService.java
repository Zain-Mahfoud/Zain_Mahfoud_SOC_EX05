package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.web.model.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto getProductById(UUID productId);

    List<ProductDto> getProductsByPriceBetween(Double from, Double to);

    ProductDto saveNewProduct(ProductDto productDto);

    void updateProduct(UUID productId, ProductDto productDto);

    void deleteById(UUID productId);
}
