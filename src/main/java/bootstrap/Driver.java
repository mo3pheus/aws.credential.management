package bootstrap;

import bootstrap.auth.AuthUtil;
import domain.ApplicationConfig;
import domain.AwsS3Credentials;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s3.S3Uploader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Driver {
    public static Logger logger = LoggerFactory.getLogger(Driver.class);
    private static ApplicationConfig applicationConfig;

    public static void main(String[] args) {
        try {
            applicationConfig = new ApplicationConfig(args);
            configureLogging(applicationConfig.getDebugLogging());
            logger.info("Running cred experiments");
            AwsS3Credentials awsS3Credentials = AuthUtil.getSecret(applicationConfig.getS3SecretName());
            S3Uploader s3Uploader = new S3Uploader(applicationConfig, awsS3Credentials);
            s3Uploader.uploadFileToS3();
        } catch (Exception e) {
            logger.error("Some other exception", e);
        }
    }

    public static String configureLogging(boolean debug) {
        FileAppender fa = new FileAppender();

        if (!debug) {
            fa.setThreshold(Level.toLevel(Priority.INFO_INT));
            fa.setFile(applicationConfig.getLogDirectory() + "/cred.experiments.log");
        } else {
            fa.setThreshold(Level.toLevel(Priority.DEBUG_INT));
            fa.setFile(applicationConfig.getLogDirectory() + "/cred.experiments.debug.log");
        }

        fa.setLayout(new EnhancedPatternLayout("%-6d [%25.35t] %-5p %40.80c - %m%n"));

        fa.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(fa);
        return fa.getFile();
    }

    public static Properties getProjectProperties(String propertiesFilePath) throws IOException {
        logger.info("Properties file specified at location = " + propertiesFilePath);
        FileInputStream projFile = new FileInputStream(propertiesFilePath);
        Properties properties = new Properties();
        properties.load(projFile);
        return properties;
    }
}
