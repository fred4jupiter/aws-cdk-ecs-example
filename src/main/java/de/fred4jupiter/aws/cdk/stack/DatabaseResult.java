package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.secretsmanager.Secret;

public class DatabaseResult {

    private DatabaseInstance databaseInstance;

    private Secret secret;

    public DatabaseResult(DatabaseInstance databaseInstance, Secret secret) {
        this.databaseInstance = databaseInstance;
        this.secret = secret;
    }

    public DatabaseInstance getDatabaseInstance() {
        return databaseInstance;
    }

    public Secret getSecret() {
        return secret;
    }
}
