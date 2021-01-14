package de.unikassel.soc.platform.web.mappers;

import de.unikassel.soc.platform.domain.Customer;
import de.unikassel.soc.platform.web.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDto dto);

    CustomerDto customerToCustomerDto(Customer customer);
}
