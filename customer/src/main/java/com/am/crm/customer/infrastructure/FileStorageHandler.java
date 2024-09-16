package com.am.crm.customer.infrastructure;

import java.util.UUID;

public interface FileStorageHandler {
    String generatePresignedPutUrl(UUID customerId);
    String generatePresignedGetUrl(UUID customerId);
}
