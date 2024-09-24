package com.am.crm.customer.features.command.update;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.exception.EntityNotFoundException;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.security.SecurityHandler;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.UpdateCustomerCommand;
import com.am.crm.customer.web.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateCustomerHandlerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FileStorageHandler fileStorageHandler;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private UpdateCustomerHandler updateCustomerHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldUpdateCustomerFieldsWithoutPhotoChange() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(UUID.fromString(customerId));
        existingCustomer.setName("Old Name");
        existingCustomer.setSurname("Old Surname");

        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.setName("New Name");
        command.setSurname("New Surname");
        command.setChangePhoto(false);

        CustomerQuery expectedQuery = new CustomerQuery();
        expectedQuery.setName("New Name");
        expectedQuery.setSurname("New Surname");

        when(customerRepository.findCustomerByCustomerId(any(UUID.class))).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(expectedQuery);

        try (MockedStatic<SecurityHandler> securityHandlerMock = mockStatic(SecurityHandler.class)) {
            securityHandlerMock.when(SecurityHandler::getCurrentUsername).thenReturn("test_user");

            // Act
            CustomerQuery result = updateCustomerHandler.handle(customerId, command);

            // Assert
            assertNotNull(result);
            assertEquals("New Name", result.getName());
            assertEquals("New Surname", result.getSurname());
            verify(customerRepository).save(argThat(customer ->
                    "New Name".equals(customer.getName()) &&
                            "New Surname".equals(customer.getSurname()) &&
                            "test_user".equals(customer.getUpdatedBy())
            ));
            verify(fileStorageHandler, never()).generatePresignedPutUrl(any());
        }
    }

    @Test
    void handle_ShouldUpdateCustomerFieldsWithPhotoChange() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(UUID.fromString(customerId));

        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.setChangePhoto(true);

        CustomerQuery expectedQuery = new CustomerQuery();
        expectedQuery.setPhotoUrl("http://presigned-url");

        when(customerRepository.findCustomerByCustomerId(any(UUID.class))).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(expectedQuery);
        when(fileStorageHandler.generatePresignedPutUrl(any())).thenReturn("http://presigned-url");

        try (MockedStatic<SecurityHandler> securityHandlerMock = mockStatic(SecurityHandler.class)) {
            securityHandlerMock.when(SecurityHandler::getCurrentUsername).thenReturn("test_user");

            // Act
            CustomerQuery result = updateCustomerHandler.handle(customerId, command);

            // Assert
            assertNotNull(result);
            assertEquals("http://presigned-url", result.getPhotoUrl());
            verify(customerRepository).save(argThat(customer ->
                    "test_user".equals(customer.getUpdatedBy())
            ));
            verify(fileStorageHandler).generatePresignedPutUrl(UUID.fromString(customerId));
        }
    }

    @Test
    void handle_ShouldThrowEntityNotFoundExceptionWhenCustomerNotFound() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        UpdateCustomerCommand command = new UpdateCustomerCommand();

        when(customerRepository.findCustomerByCustomerId(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> updateCustomerHandler.handle(customerId, command));
        verify(customerRepository, never()).save(any(Customer.class));
        verify(fileStorageHandler, never()).generatePresignedPutUrl(any());
    }

    @Test
    void handle_ShouldThrowIllegalArgumentExceptionForInvalidUUID() {
        // Arrange
        String invalidCustomerId = "invalid-uuid";
        UpdateCustomerCommand command = new UpdateCustomerCommand();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateCustomerHandler.handle(invalidCustomerId, command));
        verify(customerRepository, never()).findCustomerByCustomerId(any(UUID.class));
        verify(customerRepository, never()).save(any(Customer.class));
        verify(fileStorageHandler, never()).generatePresignedPutUrl(any());
    }
}
