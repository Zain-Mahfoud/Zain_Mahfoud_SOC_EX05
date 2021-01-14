package de.unikassel.soc.platform.web.model;

import de.unikassel.soc.platform.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDto {

    private UUID id;

    @NotBlank
    private String name;

    private List<ProductDto> products;
}
