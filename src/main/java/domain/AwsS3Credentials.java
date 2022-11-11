package domain;

public class AwsS3Credentials {
    private transient String s3AccessKey;
    private transient String s3SecretKey;

    public String getS3AccessKey() {
        return s3AccessKey;
    }

    public String getS3SecretKey() {
        return s3SecretKey;
    }
}
