package com.am.crm.customer.infrastructure;

public interface FileStorageHandler {
    String generatePresignedPutUrl(String objectKey);
    String generatePresignedGetUrl(String objectKey);
}
