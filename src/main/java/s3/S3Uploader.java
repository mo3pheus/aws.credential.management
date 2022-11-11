package s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import domain.ApplicationConfig;
import domain.AwsS3Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class S3Uploader {
    private ApplicationConfig applicationConfig;
    private Logger logger = LoggerFactory.getLogger(S3Uploader.class);
    private CredentialsProvider credentialsProvider;
    private AwsS3Credentials awsS3Credentials;

    private class Credentials implements AWSCredentials {
        AwsS3Credentials awsS3Credentials;

        Credentials(AwsS3Credentials awsS3Credentials) {
            this.awsS3Credentials = awsS3Credentials;
        }

        @Override
        public String getAWSAccessKeyId() {
            return awsS3Credentials.getS3AccessKey();
        }

        @Override
        public String getAWSSecretKey() {
            return awsS3Credentials.getS3SecretKey();
        }
    }

    private class CredentialsProvider implements AWSCredentialsProvider {
        Credentials credentials;

        CredentialsProvider(Credentials credentials) {
            this.credentials = credentials;
        }

        @Override
        public AWSCredentials getCredentials() {
            return credentials;
        }

        @Override
        public void refresh() {
        }
    }


    public S3Uploader(ApplicationConfig applicationConfig, AwsS3Credentials awsS3Credentials) {
        this.applicationConfig = applicationConfig;
        this.awsS3Credentials = awsS3Credentials;
        this.credentialsProvider = new CredentialsProvider(new Credentials(awsS3Credentials));
    }

    public void uploadFileToS3() {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_1).build();
        s3.putObject(applicationConfig.getS3BucketName(),
                "credExperiment_" + System.currentTimeMillis() + ".txt",
                getPayload());
        s3.shutdown();
    }

    private String getPayload() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> content = Files.readAllLines(Paths.get(applicationConfig.getLocalFilename()));

            for (String line : content) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (IOException io) {
            logger.error("Could not read local file. " + applicationConfig.getLocalFilename(), io);
            return null;
        }
    }
}
