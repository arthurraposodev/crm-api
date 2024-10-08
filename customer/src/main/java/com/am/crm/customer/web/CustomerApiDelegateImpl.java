package com.am.crm.customer.web;

import com.am.crm.customer.api.V1ApiDelegate;
import com.am.crm.customer.features.command.create.CreateCustomerHandler;
import com.am.crm.customer.features.command.delete.DeleteCustomerHandler;
import com.am.crm.customer.features.query.get.GetCustomerByIdHandler;
import com.am.crm.customer.features.query.list.ListCustomersHandler;
import com.am.crm.customer.features.command.update.UpdateCustomerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerApiDelegateImpl implements V1ApiDelegate {

    private final CreateCustomerHandler createCustomerHandler;
    private final UpdateCustomerHandler updateCustomerHandler;
    private final ListCustomersHandler listCustomersHandler;
    private final GetCustomerByIdHandler getCustomerByIdHandler;
    private final DeleteCustomerHandler deleteCustomerHandler;

    @Override
    public ResponseEntity<List<CustomerQuery>> listCustomers() {
        return ResponseEntity.ok(listCustomersHandler.handle());
    }

    @Override
    public ResponseEntity<CustomerQuery> getCustomerById(String customerId) {
        return ResponseEntity.ok(getCustomerByIdHandler.handle(customerId));
    }

    @Override
    public ResponseEntity<CustomerQuery> createCustomer(CreateCustomerCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createCustomerHandler.handle(command));
    }

    @Override
    public ResponseEntity<CustomerQuery> updateCustomer(String customerId, UpdateCustomerCommand updateCustomerCommand) {
        return ResponseEntity.ok(updateCustomerHandler.handle(customerId, updateCustomerCommand));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(String id) {
        deleteCustomerHandler.handle(id);
        return ResponseEntity.noContent().build();
    }
}

