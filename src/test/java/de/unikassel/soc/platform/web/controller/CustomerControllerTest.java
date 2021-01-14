package de.unikassel.soc.platform.web.controller;

import de.unikassel.soc.platform.services.CustomerService;
import de.unikassel.soc.platform.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController = new CustomerController(customerService);
    }

    @Test
    void getCustomer() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        UUID uuid = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto(uuid, "Test", null);
        when(customerService.getCustomerById(uuid)).thenReturn(customerDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/" + uuid))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerNotFound() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new MvcExceptionHandler())
                .build();
        UUID uuid = UUID.randomUUID();
        when(customerService.getCustomerById(uuid)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer/" + uuid))
                .andExpect(status().isNotFound());
    }
}