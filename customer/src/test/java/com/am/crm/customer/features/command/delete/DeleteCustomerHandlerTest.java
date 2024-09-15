package com.am.crm.customer.features.command.delete;

import com.am.crm.customer.domain.Customer;
import com.am.crm.customer.exception.EntityNotFoundException;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.util.UUIDUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteCustomerHandlerTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeleteCustomerHandler deleteCustomerHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ShouldDeleteCustomerWhenFound() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        UUID uuid = UUIDUtil.toUUID(customerId);
        Customer customer = new Customer();
        customer.setCustomerId(uuid);

        when(customerRepository.findCustomerByCustomerId(uuid)).thenReturn(Optional.of(customer));

        // Act
        deleteCustomerHandler.handle(customerId);

        // Assert
        verify(customerRepository).findCustomerByCustomerId(uuid);
        verify(customerRepository).delete(customer);
    }

    @Test
    void handle_ShouldThrowEntityNotFoundExceptionWhenCustomerNotFound() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        UUID uuid = UUIDUtil.toUUID(customerId);

        when(customerRepository.findCustomerByCustomerId(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> deleteCustomerHandler.handle(customerId));
        verify(customerRepository).findCustomerByCustomerId(uuid);
        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    void handle_ShouldThrowIllegalArgumentExceptionForInvalidUUID() {
        // Arrange
        String invalidCustomerId = "invalid-uuid";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteCustomerHandler.handle(invalidCustomerId));
        verify(customerRepository, never()).findCustomerByCustomerId(any(UUID.class));
        verify(customerRepository, never()).delete(any(Customer.class));
    }
}