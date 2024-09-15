package com.am.crm.customer.web;

import com.am.crm.customer.features.command.create.CreateCustomerHandler;
import com.am.crm.customer.features.command.delete.DeleteCustomerHandler;
import com.am.crm.customer.features.command.update.UpdateCustomerHandler;
import com.am.crm.customer.features.query.get.GetCustomerByIdHandler;
import com.am.crm.customer.features.query.list.ListCustomersHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerApiDelegateImplTest {

    @Mock
    private CreateCustomerHandler createCustomerHandler;
    @Mock
    private UpdateCustomerHandler updateCustomerHandler;
    @Mock
    private ListCustomersHandler listCustomersHandler;
    @Mock
    private GetCustomerByIdHandler getCustomerByIdHandler;
    @Mock
    private DeleteCustomerHandler deleteCustomerHandler;

    private CustomerApiDelegateImpl customerApiDelegate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerApiDelegate = new CustomerApiDelegateImpl(
                createCustomerHandler, updateCustomerHandler, listCustomersHandler,
                getCustomerByIdHandler, deleteCustomerHandler);
    }

    @Test
    void testListCustomers() {
        List<CustomerQuery> customers = List.of(new CustomerQuery(), new CustomerQuery());
        when(listCustomersHandler.handle()).thenReturn(customers);

        ResponseEntity<List<CustomerQuery>> response = customerApiDelegate.listCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customers, response.getBody());
    }

    @Test
    void testGetCustomerById() {
        String customerId = UUID.randomUUID().toString();
        CustomerQuery customer = new CustomerQuery();
        when(getCustomerByIdHandler.handle(customerId)).thenReturn(customer);

        ResponseEntity<CustomerQuery> response = customerApiDelegate.getCustomerById(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testCreateCustomer() {
        CreateCustomerCommand command = new CreateCustomerCommand();
        CustomerQuery createdCustomer = new CustomerQuery();
        when(createCustomerHandler.handle(command)).thenReturn(createdCustomer);

        ResponseEntity<CustomerQuery> response = customerApiDelegate.createCustomer(command);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCustomer, response.getBody());
    }

    @Test
    void testUpdateCustomer() {
        String customerId = UUID.randomUUID().toString();
        UpdateCustomerCommand command = new UpdateCustomerCommand();
        CustomerQuery updatedCustomer = new CustomerQuery();
        when(updateCustomerHandler.handle(customerId, command)).thenReturn(updatedCustomer);

        ResponseEntity<CustomerQuery> response = customerApiDelegate.updateCustomer(customerId, command);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomer, response.getBody());
    }

    @Test
    void testDeleteCustomer() {
        String customerId = UUID.randomUUID().toString();

        ResponseEntity<Void> response = customerApiDelegate.deleteCustomer(customerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteCustomerHandler, times(1)).handle(customerId);
    }
}