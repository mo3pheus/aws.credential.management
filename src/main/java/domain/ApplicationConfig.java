package domain;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This class contains all the required objects that govern the operation of this application. Since this class
 * holds security credentials to aws, its not intended to be stored if serialized.
 */
public class ApplicationConfig {
    private String s3BucketName;
    private String logDirectory;
    private String localFilename;
    private String s3SecretName;
    private Boolean debugLogging;
    private Namespace namespace;

    /**
     * Takes command-line arguments and converts them into application configuration.
     *
     * @param args - A String array containing command-line arguments. Arguments need to be declared with -- and their
     *             value followed with a space separating them. E.g. --debug.logging false
     */
    public ApplicationConfig(String[] args) {
        this.namespace = buildNamespace(args);
        this.debugLogging = namespace.getBoolean("debug.logging");
        this.logDirectory = namespace.getString("log.directory");
        this.localFilename = namespace.getString("local.filename");
        this.s3BucketName = namespace.getString("s3.bucket.name");
        this.s3SecretName = namespace.getString("s3.secret.name");
    }

    public Namespace buildNamespace(String[] args) {
        Namespace namespace = null;
        ArgumentParser argumentParser = ArgumentParsers.newFor("Application").build()
                .defaultHelp(true)
                .description("Experimentation with aws credential management");
        argumentParser.addArgument("--s3.bucket.name")
                .help("Destination s3 bucket for file storage");
        argumentParser.addArgument("--s3.secret.name")
                .help("Secret name for s3 secrets in secret manager");
        argumentParser.addArgument("--local.filename")
                .help("Path to the local file you want to upload to s3.");
        argumentParser.addArgument("--debug.logging").type(Boolean.class)
                .setDefault(false)
                .choices(true, false).help("Enable debug level logging or not.");
        argumentParser.addArgument("--log.directory").type(String.class)
                .setDefault("/var/log/cred.experiments/")
                .help("Path to directory where application logs will be saved.");

        try {
            namespace = argumentParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            e.printStackTrace();
        }

        return namespace;
    }

    public String getS3BucketName() {
        return s3BucketName;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    public String getLogDirectory() {
        return logDirectory;
    }

    public void setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    public String getLocalFilename() {
        return localFilename;
    }

    public void setLocalFilename(String localFilename) {
        this.localFilename = localFilename;
    }

    public String getS3SecretName() {
        return s3SecretName;
    }

    public void setS3SecretName(String s3SecretName) {
        this.s3SecretName = s3SecretName;
    }

    public Boolean getDebugLogging() {
        return debugLogging;
    }

    public void setDebugLogging(Boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }
}
