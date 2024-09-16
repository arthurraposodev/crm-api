package com.am.crm.customer.features.query.get;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.exception.EntityNotFoundException;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetCustomerByIdHandlerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FileStorageHandler fileStorageHandler;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private GetCustomerByIdHandler getCustomerByIdHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldReturnCustomerQueryWithPreSignedUrl() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer();
        customer.setCustomerId(UUID.fromString(customerId));
        customer.setName("John");
        customer.setSurname("Doe");

        CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.setCustomerId(customerId);
        customerQuery.setName("John");
        customerQuery.setSurname("Doe");
        customerQuery.setPhotoUrl("photo-key");

        when(customerRepository.findCustomerByCustomerId(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerQuery);
        when(fileStorageHandler.generatePresignedGetUrl(any())).thenReturn("http://presigned-url");

        // Act
        CustomerQuery result = getCustomerByIdHandler.handle(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals("http://presigned-url", result.getPhotoUrl());

        verify(customerRepository).findCustomerByCustomerId(UUID.fromString(customerId));
        verify(customerMapper).toDto(customer);
        verify(fileStorageHandler).generatePresignedGetUrl(UUID.fromString(result.getCustomerId()));
    }

    @Test
    void handle_ShouldThrowEntityNotFoundExceptionWhenCustomerNotFound() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        when(customerRepository.findCustomerByCustomerId(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> getCustomerByIdHandler.handle(customerId));

        verify(customerRepository).findCustomerByCustomerId(UUID.fromString(customerId));
        verify(customerMapper, never()).toDto(any(Customer.class));
        verify(fileStorageHandler, never()).generatePresignedGetUrl(any());
    }

    @Test
    void handle_ShouldThrowIllegalArgumentExceptionForInvalidUUID() {
        // Arrange
        String invalidCustomerId = "invalid-uuid";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> getCustomerByIdHandler.handle(invalidCustomerId));

        verify(customerRepository, never()).findCustomerByCustomerId(any(UUID.class));
        verify(customerMapper, never()).toDto(any(Customer.class));
        verify(fileStorageHandler, never()).generatePresignedGetUrl(any(UUID.class));
    }

    @Test
    void handle_ShouldHandleNullPhotoUrl() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer();
        customer.setCustomerId(UUID.fromString(customerId));
        customer.setName("John");
        customer.setSurname("Doe");

        CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.setCustomerId(customerId);
        customerQuery.setName("John");
        customerQuery.setSurname("Doe");
        customerQuery.setPhotoUrl(null);

        when(customerRepository.findCustomerByCustomerId(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerQuery);

        // Act
        CustomerQuery result = getCustomerByIdHandler.handle(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertNull(result.getPhotoUrl());

        verify(customerRepository).findCustomerByCustomerId(UUID.fromString(customerId));
        verify(customerMapper).toDto(customer);
    }
}