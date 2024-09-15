package com.am.crm.customer.features.command.update;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.exception.EntityNotFoundException;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.security.SecurityHandler;
import com.am.crm.customer.util.UUIDUtil;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.UpdateCustomerCommand;
import com.am.crm.customer.web.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class UpdateCustomerHandler {

    private final CustomerRepository customerRepository;
    private final FileStorageHandler fileStorageHandler;
    private final CustomerMapper customerMapper;

    public CustomerQuery handle(final String customerId, final UpdateCustomerCommand command) {
        final UUID id = UUIDUtil.toUUID(customerId);
        final Customer customer = findCustomerById(id);
        updateCustomerFields(command, customer);

        if (Boolean.TRUE.equals(command.getChangePhoto())) {
            return handlePhotoUpdate(customerId, customer);
        }

        return saveAndMapCustomer(customer);
    }

    private Customer findCustomerById(final UUID customerId) {
        return customerRepository.findCustomerByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class.getSimpleName(),customerId.toString()));
    }

    private void updateCustomerFields(final UpdateCustomerCommand command, final Customer customer) {
        if (command.getName() != null) {
            customer.setName(command.getName());
        }
        if (command.getSurname() != null) {
            customer.setSurname(command.getSurname());
        }
        customer.setUpdatedBy(SecurityHandler.getCurrentUsername());
    }

    private CustomerQuery handlePhotoUpdate(final String customerId, final Customer customer) {
        final String objectKey = generatePhotoKey(customerId);
        customer.setPhotoKey(objectKey);

        final String preSignedUrl = fileStorageHandler.generatePresignedPutUrl(objectKey);
        final Customer savedCustomer = customerRepository.save(customer);

        final CustomerQuery customerDto = customerMapper.toDto(savedCustomer);
        customerDto.setPhotoUrl(preSignedUrl);
        return customerDto;
    }

    private String generatePhotoKey(final String customerId) {
        return String.format("images/%s.jpg", customerId);
    }

    private CustomerQuery saveAndMapCustomer(final Customer customer) {
        final Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }
}
