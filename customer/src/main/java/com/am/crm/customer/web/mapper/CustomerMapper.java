package com.am.crm.customer.web.mapper;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.web.CreateCustomerCommand;
import com.am.crm.customer.web.CustomerQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "photoKey", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Customer toEntity(CreateCustomerCommand createCustomerCommand);

    @Mapping(target = "photoUrl", ignore = true)
    CustomerQuery toDto(Customer customer);
}
