package de.fred4jupiter.aws.cdk.constructs;

import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;

public class DatabaseCreatorProps {

    private Vpc vpc;
    private String username;
    private String databaseName;
    private SubnetType subnetType;
    private SecurityGroup allowedInboundSecurityGroup;

    public Vpc getVpc() {
        return vpc;
    }

    public void setVpc(Vpc vpc) {
        this.vpc = vpc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public SubnetType getSubnetType() {
        return subnetType;
    }

    public void setSubnetType(SubnetType subnetType) {
        this.subnetType = subnetType;
    }

    public SecurityGroup getAllowedInboundSecurityGroup() {
        return allowedInboundSecurityGroup;
    }

    public void setAllowedInboundSecurityGroup(SecurityGroup allowedInboundSecurityGroup) {
        this.allowedInboundSecurityGroup = allowedInboundSecurityGroup;
    }
}
