package com.am.crm.customer.features.query.list;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListCustomersHandler {

    private final CustomerRepository customerRepository;
    private final FileStorageHandler fileStorageHandler;
    private final CustomerMapper customerMapper;

    public List<CustomerQuery> handle() {
        final List<Customer> customers = customerRepository.findAll();

        return mapCustomersToDtos(customers);
    }

    private List<CustomerQuery> mapCustomersToDtos(final List<Customer> customers) {
        return customers.stream()
                .map(this::mapCustomerToDtoWithPhotoUrl)
                .toList();
    }

    private CustomerQuery mapCustomerToDtoWithPhotoUrl(final Customer customer) {
        CustomerQuery customerQuery = customerMapper.toDto(customer);
        String preSignedUrl = fileStorageHandler.generatePresignedGetUrl(customer.getCustomerId());
        customerQuery.setPhotoUrl(preSignedUrl);
        return customerQuery;
    }
}
