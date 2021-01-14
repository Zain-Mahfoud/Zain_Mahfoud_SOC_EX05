package de.unikassel.soc.platform.services;

import de.unikassel.soc.platform.domain.Customer;
import de.unikassel.soc.platform.domain.Product;
import de.unikassel.soc.platform.repos.CustomerRepo;
import de.unikassel.soc.platform.web.mappers.CustomerMapper;
import de.unikassel.soc.platform.web.mappers.CustomerMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    static CustomerMapper mapper;
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepo repo;

    @BeforeAll
    static void beforeAll() {
        mapper = new CustomerMapperImpl();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(repo, mapper);
    }

    @Test
    void getCustomerByIdError() {
        UUID uuid = UUID.randomUUID();
        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerById(uuid));
    }

    @Test
    void getCustomerById() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, "Test", new ArrayList<Product>());
        when(repo.findById(uuid)).thenReturn(Optional.of(customer));
        assertEquals(customer.getName(), customerService.getCustomerById(uuid).getName());
        verify(repo, times(1)).findById(uuid);
    }
}