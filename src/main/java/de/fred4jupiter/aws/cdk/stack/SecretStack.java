package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.secretsmanager.Secret;
import software.amazon.awscdk.services.secretsmanager.SecretStringGenerator;

public class SecretStack extends Stack {

    public SecretStack(Construct scope, String id) {
        this(scope, id, null);
    }

    public SecretStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);

        final Secret secret = Secret.Builder.create(this, "database-password")
                .secretName("fredbet-db-password").build();
    }
}
