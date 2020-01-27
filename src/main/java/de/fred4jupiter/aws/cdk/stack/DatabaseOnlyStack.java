package de.fred4jupiter.aws.cdk.stack;

import de.fred4jupiter.aws.cdk.constructs.DatabaseCreator;
import de.fred4jupiter.aws.cdk.constructs.DatabaseCreatorProps;
import de.fred4jupiter.aws.cdk.constructs.DatabaseCreatorPropsBuilder;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Arrays;

public class DatabaseOnlyStack extends Stack {

    private static final String DB_NAME = "fredbetdb";
    private static final String DB_USERNAME = "fredbet";

    public DatabaseOnlyStack(Construct scope, String id) {
        this(scope, id, null);
    }

    public DatabaseOnlyStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);

        SubnetConfiguration publicSubnet = SubnetConfiguration.builder().name("public-sn").subnetType(SubnetType.PUBLIC).cidrMask(24).build();
        SubnetConfiguration isolatedSubnet = SubnetConfiguration.builder().name("isolated-sn").subnetType(SubnetType.ISOLATED).cidrMask(24).build();
        Vpc vpc = Vpc.Builder.create(this, id).maxAzs(2).subnetConfiguration(Arrays.asList(publicSubnet, isolatedSubnet)).natGateways(0).build();

        DatabaseCreatorProps databaseCreatorProps = DatabaseCreatorPropsBuilder.create().withDatabaseName(DB_NAME)
                .withUsername(DB_USERNAME).withVpc(vpc).withSubnetType(SubnetType.ISOLATED).build();

        new DatabaseCreator(this, "MyRdsDatabase", databaseCreatorProps);
    }
}
