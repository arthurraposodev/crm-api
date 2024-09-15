package com.am.crm.customer.features.command.delete;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.exception.EntityNotFoundException;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.util.UUIDUtil;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteCustomerHandler {

    private final CustomerRepository customerRepository;

    public void handle(final String id) {
        final UUID customerId = UUIDUtil.toUUID(id);
        final Customer customer = findCustomerById(customerId);
        deleteCustomer(customer);
    }

    private Customer findCustomerById(final UUID customerId) {
        return customerRepository.findCustomerByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class.getSimpleName(), customerId.toString()));
    }

    private void deleteCustomer(final Customer customer) {
        customerRepository.delete(customer);
    }
}
