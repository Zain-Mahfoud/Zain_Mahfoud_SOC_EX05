package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.web.model.SellerDto;

import java.util.List;
import java.util.UUID;

public interface SellerService {
    SellerDto getSellerById(UUID sellerId);

    List<SellerDto> getSellersByName(String sellerName);

    SellerDto saveNewSeller(SellerDto sellerDto);

    void updateSeller(UUID sellerId, SellerDto sellerDto);

    void deleteById(UUID sellerId);
}
