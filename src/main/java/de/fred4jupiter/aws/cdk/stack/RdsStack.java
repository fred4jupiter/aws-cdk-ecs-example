package de.fred4jupiter.aws.cdk.stack;


import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;

public class RdsStack extends Stack {

    public RdsStack(Construct scope, String id) {
        this(scope, id, null);
    }

    public RdsStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);
    }

    public DatabaseInstance createDatabaseInstance(String databaseName, String username, String password) {
        return DatabaseInstance.Builder.create(this, "fredbet-db")
                .backupRetention(Duration.days(3))
                .allocatedStorage(10)
                .characterSetName("UTF-8")
                .engine(DatabaseInstanceEngine.MYSQL)
                .databaseName(databaseName)
                .masterUsername(username)
                .masterUserPassword(SecretValue.plainText(password))
                .multiAz(false)
                .instanceClass(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
                .build();
    }


}
