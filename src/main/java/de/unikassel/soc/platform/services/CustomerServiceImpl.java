package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.domain.Customer;
import de.unikassel.soc.platform.repos.CustomerRepo;
import de.unikassel.soc.platform.web.mappers.CustomerMapper;
import de.unikassel.soc.platform.web.model.CustomerDto;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo repo;
    private final CustomerMapper mapper;

    @Override
    public CustomerDto getCustomerById(UUID customerId) throws NoSuchElementException {
        Customer customer = repo.findById(customerId).orElseThrow();
        return mapper.customerToCustomerDto(customer);
    }

    @Override
    public List<CustomerDto> getCustomersByName(String customerName) {
        List<Customer> customers = repo.findByName(customerName);
        return customers.stream().map(mapper::customerToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDto) {
        Customer customer = mapper.customerDtoToCustomer(customerDto);
        return mapper.customerToCustomerDto(repo.save(customer));
    }

    @Override
    public void updateCustomer(UUID customerId, CustomerDto customerDto) {
        Customer customer = mapper.customerDtoToCustomer(customerDto);
        customer.setId(customerId);
        repo.save(customer);
    }

    @Override
    public void deleteById(UUID customerId) {
        repo.deleteById(customerId);
    }
}
