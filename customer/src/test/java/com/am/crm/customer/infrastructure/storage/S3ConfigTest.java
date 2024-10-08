package com.am.crm.customer.infrastructure.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.junit.jupiter.api.Assertions.*;

class S3ConfigTest {

    private S3Config s3Config;

    @BeforeEach
    void setUp() {
        s3Config = new S3Config();
    }

    @Test
    void s3Presigner_ShouldCreateS3PresignerWithCorrectConfiguration() {
        // Act
        S3Presigner s3Presigner = s3Config.s3Presigner();

        // Assert
        assertNotNull(s3Presigner);

        // Verify the region
        Region region = (Region) ReflectionTestUtils.getField(s3Presigner, "region");
        assertEquals(Region.US_EAST_2, region);

        // Verify the credentials provider
        Object credentialsProvider = ReflectionTestUtils.getField(s3Presigner, "credentialsProvider");
        assertInstanceOf(EnvironmentVariableCredentialsProvider.class, credentialsProvider);
    }

    //As this is non-Spring-managed test
    @Test
    void s3Presigner_ShouldCreateNewInstanceOnEachCall() {
        // Act
        S3Presigner s3Presigner1 = s3Config.s3Presigner();
        S3Presigner s3Presigner2 = s3Config.s3Presigner();

        // Assert
        assertNotNull(s3Presigner1);
        assertNotNull(s3Presigner2);
        assertNotSame(s3Presigner1, s3Presigner2);
    }

    //As this is non-Spring-managed test
    @Test
    void s3Client_ShouldCreateNewInstanceOnEachCall() {
        // Act
        S3Client s3Client1 = s3Config.s3Client();
        S3Client s3Client2 = s3Config.s3Client();

        // Assert
        assertNotNull(s3Client1);
        assertNotNull(s3Client2);
        assertNotSame(s3Client1, s3Client2);
    }
}