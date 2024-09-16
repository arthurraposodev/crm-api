package com.am.crm.customer.infrastructure.storage;

import com.am.crm.customer.util.UUIDUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    @Mock
    private S3Presigner s3Presigner;

    @Mock
    private S3Client s3Client;

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
        String objectKey = "46981c0d-4497-4a50-8276-242f0cd3472d";
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/images/46981c0d-4497-4a50-8276-242f0cd3472d.jpg";
        PresignedPutObjectRequest mockPresignedRequest = mock(PresignedPutObjectRequest.class);
        when(mockPresignedRequest.url()).thenReturn(URI.create(expectedUrl).toURL());
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenReturn(mockPresignedRequest);

        // Act
        String result = s3Service.generatePresignedPutUrl(UUIDUtil.toUUID(objectKey));

        // Assert
        assertEquals(expectedUrl, result);
        verify(s3Presigner).presignPutObject(argThat((PutObjectPresignRequest request) -> {
            assertEquals(Duration.ofMinutes(15), request.signatureDuration());
            assertEquals("test-bucket", request.putObjectRequest().bucket());
            assertEquals("images/"+objectKey+".jpg", request.putObjectRequest().key());
            return true;
        }));
    }

    @Test
    void generatePresignedGetUrl_ShouldReturnPresignedUrl() throws MalformedURLException {
        // Arrange
        String objectKey = "46981c0d-4497-4a50-8276-242f0cd3472d";
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/test-object-key";
        PresignedGetObjectRequest mockPresignedRequest = mock(PresignedGetObjectRequest.class);
        when(mockPresignedRequest.url()).thenReturn(URI.create(expectedUrl).toURL());
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(mockPresignedRequest);

        // Act
        String result = s3Service.generatePresignedGetUrl(UUID.fromString(objectKey));

        // Assert
        assertEquals(expectedUrl, result);
        verify(s3Presigner).presignGetObject(argThat((GetObjectPresignRequest request) -> {
            assertEquals(Duration.ofMinutes(15), request.signatureDuration());
            assertEquals("test-bucket", request.getObjectRequest().bucket());
            assertEquals("images/"+objectKey+".jpg", request.getObjectRequest().key());
            return true;
        }));
    }

    @Test
    void generatePresignedPutUrl_ShouldThrowRuntimeExceptionOnError() {
        // Arrange
        String objectKey = "test-object-key";
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenThrow(new RuntimeException("S3 Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> s3Service.generatePresignedPutUrl(UUID.fromString(objectKey)));
    }

    @Test
    void generatePresignedGetUrl_ShouldThrowRuntimeExceptionOnError() {
        // Arrange
        String objectKey = "test-object-key";
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenThrow(new RuntimeException("S3 Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> s3Service.generatePresignedGetUrl(UUID.fromString(objectKey)));
    }
}