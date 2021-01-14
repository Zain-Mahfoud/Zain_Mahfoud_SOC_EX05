package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.domain.Product;
import de.unikassel.soc.platform.repos.ProductRepo;
import de.unikassel.soc.platform.web.mappers.ProductMapper;
import de.unikassel.soc.platform.web.mappers.ProductMapperImpl;
import de.unikassel.soc.platform.web.model.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo repo;
    private ProductMapper mapper = new ProductMapperImpl();

    @Override
    public ProductDto getProductById(UUID productId) throws NoSuchElementException {
        Product product = repo.findById(productId).orElseThrow();
        return mapper.productToProductDto(product);
    }

    @Override
    public ProductDto saveNewProduct(ProductDto productDto) {
        Product product = mapper.productDtoToProduct(productDto);
        return mapper.productToProductDto(repo.save(product));
    }

    @Override
    public void updateProduct(UUID productId, ProductDto productDto) {
        Product product = mapper.productDtoToProduct(productDto);
        product.setId(productId);
        repo.save(product);
    }

    @Override
    public void deleteById(UUID productId) {
        repo.deleteById(productId);
    }

    @Override
    public List<ProductDto> getProductsByPriceBetween(Double from, Double to) {
        List<Product> products = repo.findByPriceBetween(from, to);
        return products.stream().map(mapper::productToProductDto).collect(Collectors.toList());
    }
}
