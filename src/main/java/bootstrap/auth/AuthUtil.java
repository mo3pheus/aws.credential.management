package bootstrap.auth;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClient;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.AwsS3Credentials;

public class AuthUtil {
    public static AwsS3Credentials getSecret(String secretName) throws JsonProcessingException {
        Region region = Region.getRegion(Regions.US_EAST_1);

        AWSSecretsManager secretsManager = AWSSecretsManagerClient.builder()
                .withRegion(region.getName())
                .build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest();
        getSecretValueRequest.setSecretId(secretName);

        GetSecretValueResult getSecretValueResponse;

        getSecretValueResponse = secretsManager.getSecretValue(getSecretValueRequest);
        String secret = getSecretValueResponse.getSecretString();

        ObjectMapper objectMapper = new ObjectMapper();
        AwsS3Credentials awsS3Credentials = objectMapper.readValue(secret, AwsS3Credentials.class);

        return awsS3Credentials;
    }
}
