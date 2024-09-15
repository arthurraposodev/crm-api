package com.am.crm.customer.config;

import com.am.crm.customer.features.command.create.CreateCustomerHandler;
import com.am.crm.customer.features.command.delete.DeleteCustomerHandler;
import com.am.crm.customer.features.command.update.UpdateCustomerHandler;
import com.am.crm.customer.features.query.get.GetCustomerByIdHandler;
import com.am.crm.customer.features.query.list.ListCustomersHandler;
import com.am.crm.customer.infrastructure.repository.CustomerRepository;
import com.am.crm.customer.infrastructure.storage.S3Handler;
import com.am.crm.customer.web.mapper.CustomerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {

    @Bean
    public CreateCustomerHandler createCustomerHandler(CustomerRepository customerRepository,
                                                       CustomerMapper customerMapper,
                                                       S3Handler s3Service) {
        return new CreateCustomerHandler(customerRepository, customerMapper, s3Service);
    }

    @Bean
    public DeleteCustomerHandler deleteCustomerHandler(CustomerRepository customerRepository) {
        return new DeleteCustomerHandler(customerRepository);
    }

    @Bean
    public UpdateCustomerHandler updateCustomerHandler(CustomerRepository customerRepository,
                                                       CustomerMapper customerMapper,
                                                       S3Handler s3Service) {
        return new UpdateCustomerHandler(customerRepository, s3Service, customerMapper);
    }

    @Bean
    public GetCustomerByIdHandler getCustomerByIdHandler(CustomerRepository customerRepository,
                                                         CustomerMapper customerMapper,
                                                         S3Handler s3Service) {
        return new GetCustomerByIdHandler(customerRepository, s3Service, customerMapper);
    }

    @Bean
    public ListCustomersHandler listCustomersHandler(CustomerRepository customerRepository,
                                                     CustomerMapper customerMapper,
                                                     S3Handler s3Service) {
        return new ListCustomersHandler(customerRepository, s3Service, customerMapper);
    }
}