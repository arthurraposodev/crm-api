package com.am.crm.customer.features.command.create;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.security.SecurityHandler;
import com.am.crm.customer.web.CreateCustomerCommand;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateCustomerHandler {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final FileStorageHandler fileStorageHandler;

    public CustomerQuery handle(final CreateCustomerCommand command) {
        final Customer customer = createCustomerFromCommand(command);

        final Customer savedCustomer = customerRepository.save(customer);
        var customerQuery = customerMapper.toDto(savedCustomer);
        generatePhotoUrl(customer.getPhotoKey(), customerQuery);
        return customerQuery;
    }

    private void generatePhotoUrl(String photoKey, CustomerQuery customerQuery) {
        final String preSignedUrl = fileStorageHandler.generatePresignedPutUrl(photoKey);
        customerQuery.setPhotoUrl(preSignedUrl);
    }

    private Customer createCustomerFromCommand(final CreateCustomerCommand command) {
        final Customer customer = customerMapper.toEntity(command);
        final String username = SecurityHandler.getCurrentUsername();

        customer.setCreatedBy(username);
        customer.setUpdatedBy(username);
        customer.setCustomerId(UUID.randomUUID());
        customer.setPhotoKey(generatePhotoKey(customer.getCustomerId()));

        return customer;
    }

    private String generatePhotoKey(final UUID customerId) {
        return String.format("images/%s.jpg", customerId);
    }

}
