package co.com.bancolombia.secretmanager;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.net.URI;

@Service
public class SecretManagerAws implements SecretManager {


    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper objectMapper;

    public SecretManagerAws(@Value("${aws.region}") String region,
                            @Value("${aws.localstack.endpoint}") String url, ObjectMapper objectMapper) {
        this.secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(url))
                .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T getSecret(String secretName, Class<T> clazz) {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        String secretString = getSecretValueResponse.secretString();

        try {
            JsonNode jsonNode = objectMapper.readTree(secretString);
            return objectMapper.convertValue(jsonNode, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir el secreto: " + e.getMessage(), e);
        }
    }
}
