package de.unikassel.soc.platform.web.controller;

import de.unikassel.soc.platform.services.SellerService;
import de.unikassel.soc.platform.web.model.SellerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/seller")
@RestController
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerDto> getSeller(@PathVariable("sellerId")  UUID sellerId){
        return new ResponseEntity<>(sellerService.getSellerById(sellerId), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<SellerDto>> getSeller(@RequestParam("name") String sellerName){
        return new ResponseEntity<>(sellerService.getSellersByName(sellerName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody @Validated SellerDto sellerDto){
        SellerDto savedDto = sellerService.saveNewSeller(sellerDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/api/v1/seller/" + savedDto.getId().toString());

        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{sellerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleUpdate(@PathVariable("sellerId") UUID sellerId, @Validated @RequestBody SellerDto sellerDto){
        sellerService.updateSeller(sellerId, sellerDto);
    }

    @DeleteMapping("/{sellerId}")
    public void deleteById(@PathVariable("sellerId") UUID sellerId){
        sellerService.deleteById(sellerId);
    }

}
