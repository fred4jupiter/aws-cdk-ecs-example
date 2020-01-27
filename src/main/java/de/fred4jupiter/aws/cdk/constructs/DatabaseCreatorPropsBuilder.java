package de.fred4jupiter.aws.cdk.constructs;

import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;

public final class DatabaseCreatorPropsBuilder {
    private Vpc vpc;
    private String username;
    private String databaseName;
    private SubnetType subnetType;
    private SecurityGroup allowedInboundSecurityGroup;

    private DatabaseCreatorPropsBuilder() {
    }

    public static DatabaseCreatorPropsBuilder create() {
        return new DatabaseCreatorPropsBuilder();
    }

    public DatabaseCreatorPropsBuilder withVpc(Vpc vpc) {
        this.vpc = vpc;
        return this;
    }

    public DatabaseCreatorPropsBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public DatabaseCreatorPropsBuilder withDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public DatabaseCreatorPropsBuilder withSubnetType(SubnetType subnetType) {
        this.subnetType = subnetType;
        return this;
    }

    public DatabaseCreatorPropsBuilder withAllowedInboundSecurityGroup(SecurityGroup allowedInboundSecurityGroup) {
        this.allowedInboundSecurityGroup = allowedInboundSecurityGroup;
        return this;
    }

    public DatabaseCreatorProps build() {
        DatabaseCreatorProps databaseCreatorProps = new DatabaseCreatorProps();
        databaseCreatorProps.setVpc(vpc);
        databaseCreatorProps.setUsername(username);
        databaseCreatorProps.setDatabaseName(databaseName);
        databaseCreatorProps.setSubnetType(subnetType);
        databaseCreatorProps.setAllowedInboundSecurityGroup(allowedInboundSecurityGroup);
        return databaseCreatorProps;
    }
}
