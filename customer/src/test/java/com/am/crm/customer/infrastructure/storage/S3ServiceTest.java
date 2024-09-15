package com.am.crm.customer.infrastructure.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    @Mock
    private S3Presigner s3Presigner;

    @InjectMocks
    private S3Handler s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(s3Service, "bucketName", "test-bucket");
    }

    @Test
    void generatePresignedPutUrl_ShouldReturnPresignedUrl() throws MalformedURLException {
        // Arrange
        String objectKey = "test-object-key";
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/test-object-key";
        PresignedPutObjectRequest mockPresignedRequest = mock(PresignedPutObjectRequest.class);
        when(mockPresignedRequest.url()).thenReturn(URI.create(expectedUrl).toURL());
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenReturn(mockPresignedRequest);

        // Act
        String result = s3Service.generatePresignedPutUrl(objectKey);

        // Assert
        assertEquals(expectedUrl, result);
        verify(s3Presigner).presignPutObject((PutObjectPresignRequest) argThat((PutObjectPresignRequest request) -> {
            assertEquals(Duration.ofMinutes(15), request.signatureDuration());
            assertEquals("test-bucket", request.putObjectRequest().bucket());
            assertEquals(objectKey, request.putObjectRequest().key());
            return true;
        }));
    }

    @Test
    void generatePresignedGetUrl_ShouldReturnPresignedUrl() throws MalformedURLException {
        // Arrange
        String objectKey = "test-object-key";
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/test-object-key";
        PresignedGetObjectRequest mockPresignedRequest = mock(PresignedGetObjectRequest.class);
        when(mockPresignedRequest.url()).thenReturn(URI.create(expectedUrl).toURL());
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(mockPresignedRequest);

        // Act
        String result = s3Service.generatePresignedGetUrl(objectKey);

        // Assert
        assertEquals(expectedUrl, result);
        verify(s3Presigner).presignGetObject((GetObjectPresignRequest) argThat((GetObjectPresignRequest request) -> {
            assertEquals(Duration.ofMinutes(15), request.signatureDuration());
            assertEquals("test-bucket", request.getObjectRequest().bucket());
            assertEquals(objectKey, request.getObjectRequest().key());
            return true;
        }));
    }

    @Test
    void generatePresignedPutUrl_ShouldThrowRuntimeExceptionOnError() {
        // Arrange
        String objectKey = "test-object-key";
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenThrow(new RuntimeException("S3 Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> s3Service.generatePresignedPutUrl(objectKey));
    }

    @Test
    void generatePresignedGetUrl_ShouldThrowRuntimeExceptionOnError() {
        // Arrange
        String objectKey = "test-object-key";
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenThrow(new RuntimeException("S3 Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> s3Service.generatePresignedGetUrl(objectKey));
    }
}