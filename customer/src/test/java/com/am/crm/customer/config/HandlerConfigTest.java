package com.am.crm.customer.config;

import com.am.crm.customer.features.command.create.CreateCustomerHandler;
import com.am.crm.customer.features.command.delete.DeleteCustomerHandler;
import com.am.crm.customer.features.command.update.UpdateCustomerHandler;
import com.am.crm.customer.features.query.get.GetCustomerByIdHandler;
import com.am.crm.customer.features.query.list.ListCustomersHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.infrastructure.storage.S3Handler;
import com.am.crm.customer.web.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HandlerConfigTest {

    // Mock dependencies
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private S3Handler s3Service;

    private HandlerConfig handlerConfig;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        // Initialize the configuration class
        handlerConfig = new HandlerConfig();
    }

    @Test
    void testCreateCustomerHandler() {
        // Test if CreateCustomerHandler bean is correctly created
        CreateCustomerHandler createCustomerHandler = handlerConfig.createCustomerHandler(customerRepository, customerMapper, s3Service);
        assertNotNull(createCustomerHandler, "CreateCustomerHandler should not be null");
    }

    @Test
    void testDeleteCustomerHandler() {
        // Test if DeleteCustomerHandler bean is correctly created
        DeleteCustomerHandler deleteCustomerHandler = handlerConfig.deleteCustomerHandler(customerRepository);
        assertNotNull(deleteCustomerHandler, "DeleteCustomerHandler should not be null");
    }

    @Test
    void testUpdateCustomerHandler() {
        // Test if UpdateCustomerHandler bean is correctly created
        UpdateCustomerHandler updateCustomerHandler = handlerConfig.updateCustomerHandler(customerRepository, customerMapper, s3Service);
        assertNotNull(updateCustomerHandler, "UpdateCustomerHandler should not be null");
    }

    @Test
    void testGetCustomerByIdHandler() {
        // Test if GetCustomerByIdHandler bean is correctly created
        GetCustomerByIdHandler getCustomerByIdHandler = handlerConfig.getCustomerByIdHandler(customerRepository, customerMapper, s3Service);
        assertNotNull(getCustomerByIdHandler, "GetCustomerByIdHandler should not be null");
    }

    @Test
    void testListCustomersHandler() {
        // Test if ListCustomersHandler bean is correctly created
        ListCustomersHandler listCustomersHandler = handlerConfig.listCustomersHandler(customerRepository, customerMapper, s3Service);
        assertNotNull(listCustomersHandler, "ListCustomersHandler should not be null");
    }
}

