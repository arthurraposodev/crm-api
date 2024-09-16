package com.am.crm.customer.features.query.list;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.infrastructure.FileStorageHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.web.CustomerQuery;
import com.am.crm.customer.web.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ListCustomersHandlerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FileStorageHandler fileStorageHandler;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private ListCustomersHandler listCustomersHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldReturnListOfCustomerQueriesWithPreSignedUrls() {
        // Arrange
        Customer customer1 = new Customer();
        customer1.setCustomerId(UUID.randomUUID());
        customer1.setName("John");
        customer1.setSurname("Doe");

        Customer customer2 = new Customer();
        customer2.setCustomerId(UUID.randomUUID());
        customer2.setName("Jane");
        customer2.setSurname("Smith");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        CustomerQuery customerQuery1 = new CustomerQuery();
        customerQuery1.setCustomerId(customer1.getCustomerId().toString());
        customerQuery1.setName("John");
        customerQuery1.setSurname("Doe");
        customerQuery1.setPhotoUrl("http://presigned-url-1");

        CustomerQuery customerQuery2 = new CustomerQuery();
        customerQuery2.setCustomerId(customer2.getCustomerId().toString());
        customerQuery2.setName("Jane");
        customerQuery2.setSurname("Smith");
        customerQuery2.setPhotoUrl("http://presigned-url-2");

        when(customerRepository.findAll()).thenReturn(customers);
        when(fileStorageHandler.generatePresignedGetUrl(customer1.getCustomerId())).thenReturn("http://presigned-url-1");
        when(fileStorageHandler.generatePresignedGetUrl(customer2.getCustomerId())).thenReturn("http://presigned-url-2");
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerQuery1, customerQuery2);

        // Act
        List<CustomerQuery> result = listCustomersHandler.handle();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        CustomerQuery resultQuery1 = result.get(0);
        assertEquals("John", resultQuery1.getName());
        assertEquals("Doe", resultQuery1.getSurname());
        assertEquals("http://presigned-url-1", resultQuery1.getPhotoUrl());

        CustomerQuery resultQuery2 = result.get(1);
        assertEquals("Jane", resultQuery2.getName());
        assertEquals("Smith", resultQuery2.getSurname());
        assertEquals("http://presigned-url-2", resultQuery2.getPhotoUrl());

        verify(customerRepository).findAll();
        verify(fileStorageHandler, times(2)).generatePresignedGetUrl(any(UUID.class));
        verify(customerMapper, times(2)).toDto(any(Customer.class));
    }

    @Test
    void handle_ShouldReturnEmptyListWhenNoCustomersFound() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(List.of());

        // Act
        List<CustomerQuery> result = listCustomersHandler.handle();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(customerRepository).findAll();
        verify(fileStorageHandler, never()).generatePresignedGetUrl(any(UUID.class));
        verify(customerMapper, never()).toDto(any(Customer.class));
    }
}