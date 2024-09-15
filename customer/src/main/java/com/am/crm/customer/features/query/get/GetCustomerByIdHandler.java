package com.am.crm.customer.features.query.get;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.exception.EntityNotFoundException;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.util.UUIDUtil;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetCustomerByIdHandler {

    private final CustomerRepository customerRepository;
    private final FileStorageHandler fileStorageHandler;
    private final CustomerMapper customerMapper;

    public CustomerQuery handle(final String id) {
        final UUID customerId = UUIDUtil.toUUID(id);
        final CustomerQuery customerQuery = findCustomerById(customerId);
        final String preSignedUrl = fileStorageHandler.generatePresignedGetUrl(customerQuery.getPhotoUrl());

        customerQuery.setPhotoUrl(preSignedUrl);
        return customerQuery;
    }

    private CustomerQuery findCustomerById(final UUID customerId) {
        return customerRepository.findCustomerByCustomerId(customerId)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class.getSimpleName(), customerId.toString()));
    }
}
