package de.fred4jupiter.aws.cdk.stack;


import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.secretsmanager.Secret;

public class RdsStack extends Stack {

    public RdsStack(Construct scope, String id) {
        this(scope, id, null);
    }

    public RdsStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
    }

    public DatabaseResult createDatabaseInstance(VpcStack vpcStack, String databaseName, String username) {
        Secret secret = Secret.Builder.create(this, "database-password").build();

        DatabaseInstance databaseInstance = DatabaseInstance.Builder.create(this, "fredbet-db")
                .vpc(vpcStack.getVpc())
                .backupRetention(Duration.days(3))
                .allocatedStorage(10)
                .characterSetName("UTF-8")
                .engine(DatabaseInstanceEngine.MYSQL)
                .databaseName(databaseName)
                .masterUsername(username)
                .masterUserPassword(secret.getSecretValue())
                .multiAz(false)
                .instanceClass(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
                .vpcPlacement(SubnetSelection.builder().subnetType(SubnetType.PRIVATE).build())
                .build();
        return new DatabaseResult(databaseInstance, secret);
    }
}
