package de.unikassel.soc.platform.repos;

import de.unikassel.soc.platform.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, UUID> {
    List<Product> findByPriceBetween(Double from, Double to);
}
