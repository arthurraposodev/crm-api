package com.am.crm.user.infrastructure.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CognitoConfig.class)
@Import(CognitoConfig.class)
@TestPropertySource(properties = {
        "aws.accessKeyId=test",
        "aws.secretKey=test",
        "aws.region=us-east-1"
})
class CognitoConfigTest {

    @Autowired
    private CognitoIdentityProviderClient cognitoIdentityProviderClient;

    @Test
    void cognitoIdentityProviderClient_shouldBeConfigured() {
        assertNotNull(cognitoIdentityProviderClient);
    }
}