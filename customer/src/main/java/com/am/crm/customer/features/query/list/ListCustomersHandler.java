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

        customers.forEach(this::attachPreSignedUrlToCustomer);

        return mapCustomersToDto(customers);
    }

    private void attachPreSignedUrlToCustomer(final Customer customer) {
        final String preSignedUrl = fileStorageHandler.generatePresignedGetUrl(customer.getPhotoKey());
        customer.setPhotoKey(preSignedUrl);  // Replace the object key with the pre-signed URL for response
    }

    private List<CustomerQuery> mapCustomersToDto(final List<Customer> customers) {
        return customers.stream()
                .map(customerMapper::toDto)
                .toList();
    }
}
