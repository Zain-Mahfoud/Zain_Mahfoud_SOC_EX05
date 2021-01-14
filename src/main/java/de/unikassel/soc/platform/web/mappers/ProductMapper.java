package de.unikassel.soc.platform.web.mappers;

import de.unikassel.soc.platform.domain.Product;
import de.unikassel.soc.platform.web.model.ProductDto;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto dto);
}
