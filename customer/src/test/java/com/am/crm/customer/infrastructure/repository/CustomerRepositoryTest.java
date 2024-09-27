package com.am.crm.customer.infrastructure.repository;

import com.am.crm.customer.domain.Customer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CustomerRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @Autowired
    private CustomerRepository customerRepository;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.hikari.max-lifetime", ()-> "10000");
        registry.add("spring.datasource.hikari.idle-timeout", ()-> "10000");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }

    @Test
    void testSaveAndFindById() {
        Customer customer = new Customer();
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setCustomerId(UUID.randomUUID());
        customer.setUpdatedBy("username");
        customer.setCreatedBy("username");

        Customer savedCustomer = customerRepository.save(customer);
        assertNotNull(savedCustomer.getId());

        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        assertTrue(foundCustomer.isPresent());
        assertEquals(customer.getName(), foundCustomer.get().getName());
        assertEquals(customer.getSurname(), foundCustomer.get().getSurname());
        assertEquals(customer.getCustomerId(), foundCustomer.get().getCustomerId());
    }

    @Test
    void testFindByCustomerId() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setName("Jane");
        customer.setSurname("Smith");
        customer.setCustomerId(customerId);
        customer.setUpdatedBy("username");
        customer.setCreatedBy("username");

        customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findCustomerByCustomerId(customerId);
        assertTrue(foundCustomer.isPresent());
        assertEquals(customer.getName(), foundCustomer.get().getName());
        assertEquals(customer.getSurname(), foundCustomer.get().getSurname());
    }
}