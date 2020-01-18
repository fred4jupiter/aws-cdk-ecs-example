package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Arrays;

public class VpcStack extends Stack {

    private final Vpc vpc;

    private final SubnetConfiguration publicSubnet;

    private final SubnetConfiguration privateSubnet;

    private final SubnetConfiguration isolatedSubnet;

    public VpcStack(Construct scope, String id) {
        this(scope, id, null);
    }

    public VpcStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);

        // Default is all AZs in region
        this.publicSubnet = SubnetConfiguration.builder().name("public-sn").subnetType(SubnetType.PUBLIC).cidrMask(24).build();
        this.privateSubnet = SubnetConfiguration.builder().name("private-sn").subnetType(SubnetType.PRIVATE).cidrMask(24).build();
        this.isolatedSubnet = SubnetConfiguration.builder().name("isolated-sn").subnetType(SubnetType.ISOLATED).cidrMask(24).build();

        this.vpc = Vpc.Builder.create(this, "MyVpc").maxAzs(2).subnetConfiguration(Arrays.asList(publicSubnet, privateSubnet, isolatedSubnet)).natGateways(1).build();
    }

    public Vpc getVpc() {
        return vpc;
    }

    public SubnetConfiguration getPublicSubnet() {
        return publicSubnet;
    }

    public SubnetConfiguration getIsolatedSubnet() {
        return isolatedSubnet;
    }

    public SubnetConfiguration getPrivateSubnet() {
        return privateSubnet;
    }
}
