package com.am.crm.customer.infrastructure.repository;

import com.am.crm.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByCustomerId(UUID customerId);
}
