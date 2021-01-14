package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.domain.Seller;
import de.unikassel.soc.platform.repos.SellerRepo;
import de.unikassel.soc.platform.web.mappers.SellerMapper;
import de.unikassel.soc.platform.web.model.SellerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepo repo;
    private final SellerMapper mapper;

    @Override
    public SellerDto getSellerById(UUID sellerId) throws NoSuchElementException {
        Seller seller = repo.findById(sellerId).orElseThrow();
        return mapper.sellerToSellerDto(seller);
    }

    @Override
    public List<SellerDto> getSellersByName(String sellerName) {
        List<Seller> sellers = repo.findByName(sellerName);
        return sellers.stream().map(mapper::sellerToSellerDto).collect(Collectors.toList());
    }

    @Override
    public SellerDto saveNewSeller(SellerDto sellerDto) {
        Seller seller = mapper.sellerDtoToSeller(sellerDto);
        return mapper.sellerToSellerDto(repo.save(seller));
    }

    @Override
    public void updateSeller(UUID sellerId, SellerDto sellerDto) {
        Seller seller = mapper.sellerDtoToSeller(sellerDto);
        seller.setId(sellerId);
        repo.save(seller);
    }

    @Override
    public void deleteById(UUID sellerId) {
        repo.deleteById(sellerId);
    }
}
