package com.am.crm.customer.features.command.create;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.security.SecurityHandler;
import com.am.crm.customer.web.CreateCustomerCommand;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateCustomerHandlerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Mock
    private FileStorageHandler fileStorageHandler;

    @InjectMocks
    private CreateCustomerHandler createCustomerHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldCreateCustomerAndGeneratePreSignedUrl() {
        // Arrange
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setName("John");
        command.setSurname("Doe");

        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setName("John");
        customer.setSurname("Doe");

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(customer.getCustomerId());
        savedCustomer.setName(customer.getName());
        savedCustomer.setSurname(customer.getSurname());

        CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.setName("John");
        customerQuery.setSurname("Doe");

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(fileStorageHandler.generatePresignedPutUrl(anyString())).thenReturn("http://presigned-url");

        // Act
        CustomerQuery result = createCustomerHandler.handle(command);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals("http://presigned-url", result.getPhotoUrl());

        // Verify that customerRepository.save was called with the correct customer
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();
        assertEquals("John", capturedCustomer.getName());
        assertEquals("Doe", capturedCustomer.getSurname());
        assertNotNull(capturedCustomer.getCustomerId());
        assertNotNull(capturedCustomer.getPhotoKey());

        // Verify that pre-signed URL was generated
        verify(fileStorageHandler).generatePresignedPutUrl(capturedCustomer.getPhotoKey());
    }

    @Test
    void handle_ShouldSetCreatedByAndUpdatedByCorrectly() {
        // Arrange
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setName("John");
        command.setSurname("Doe");

        Customer customer = new Customer();
        customer.setName("John");
        customer.setSurname("Doe");

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(customer.getCustomerId());
        savedCustomer.setName(customer.getName());
        savedCustomer.setSurname(customer.getSurname());

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        MockedStatic<SecurityHandler> securityHandlerMock = Mockito.mockStatic(SecurityHandler.class);
        securityHandlerMock.when(SecurityHandler::getCurrentUsername).thenReturn("test_user");

        // Act
        createCustomerHandler.handle(command);

        // Assert
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();

        assertEquals(customer.getName(), capturedCustomer.getName());
        assertEquals(customer.getSurname(), capturedCustomer.getSurname());
        assertEquals("test_user", capturedCustomer.getCreatedBy());
        assertEquals("test_user", capturedCustomer.getUpdatedBy());
        securityHandlerMock.close();
    }
}
