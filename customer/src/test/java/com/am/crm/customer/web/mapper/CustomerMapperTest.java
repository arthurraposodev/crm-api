package com.am.crm.customer.web.mapper;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.web.CreateCustomerCommand;
import com.am.crm.customer.web.CustomerQuery;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerMapperTest {

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void testToEntity() {
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setName("John");
        command.setSurname("Doe");

        Customer customer = mapper.toEntity(command);

        assertEquals("John", customer.getName());
        assertEquals("Doe", customer.getSurname());
        assertNull(customer.getId());
        assertNull(customer.getCustomerId());
        assertNull(customer.getCreatedBy());
        assertNull(customer.getUpdatedBy());
        assertNull(customer.getCreatedAt());
        assertNull(customer.getUpdatedAt());
    }

    @Test
    void testToDto() {
        Customer customer = new Customer();
        customer.setName("Jane");
        customer.setSurname("Smith");
        customer.setCustomerId(UUID.randomUUID());

        CustomerQuery query = mapper.toDto(customer);

        assertEquals("Jane", query.getName());
        assertEquals("Smith", query.getSurname());
        assertEquals(customer.getCustomerId().toString(), query.getCustomerId());
        assertNull(query.getPhotoUrl());
    }
}
