package de.fred4jupiter.aws.cdk.constructs;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.secretsmanager.Secret;

import java.util.Collections;

public class DatabaseCreator extends Construct {

    private final String dbInstanceEndpointAddress;

    private final String dbInstanceEndpointPort;

    private final Secret secret;

    private final DatabaseCreatorProps props;

    public DatabaseCreator(Construct scope, String id, DatabaseCreatorProps props) {
        super(scope, id);
        this.props = props;

//        SecurityGroup securityGroup = SecurityGroup.Builder.create(this, "db-sg").securityGroupName("db-sg").build();
//        securityGroup.addIngressRule(props.getAllowedInboundSecurityGroup(), Port.tcp(3306));

        final Secret secret = Secret.Builder.create(this, "database-password")
                .secretName("fredbet-db-password").build();

        final DatabaseInstance databaseInstance = DatabaseInstance.Builder.create(this, id)
                .vpc(props.getVpc())
                .backupRetention(Duration.days(3))
                .allocatedStorage(10)
                .engine(DatabaseInstanceEngine.MYSQL)
                .databaseName(props.getDatabaseName())
                .masterUsername(props.getUsername())
                .masterUserPassword(secret.getSecretValue())
                .multiAz(false)
                .instanceClass(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
                .vpcPlacement(SubnetSelection.builder().subnetType(props.getSubnetType()).build())
                .availabilityZone("eu-central-1a")
//                .securityGroups(Collections.singletonList(securityGroup))
                .build();
        this.dbInstanceEndpointAddress = databaseInstance.getDbInstanceEndpointAddress();
        this.dbInstanceEndpointPort = databaseInstance.getDbInstanceEndpointPort();
        this.secret = secret;
    }

    public Secret getSecret() {
        return secret;
    }

    public String getDbInstanceEndpointAddress() {
        return dbInstanceEndpointAddress;
    }

    public String getDbInstanceEndpointPort() {
        return dbInstanceEndpointPort;
    }

    public String getJdbcUrl() {
        return "jdbc:mysqldb://" + getDbInstanceEndpointAddress() + ":" + getDbInstanceEndpointPort() + "/" + props.getDatabaseName();
    }
}
