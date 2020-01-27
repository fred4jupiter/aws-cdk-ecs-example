package de.fred4jupiter.aws.cdk.stack;

import de.fred4jupiter.aws.cdk.constructs.DatabaseCreator;
import de.fred4jupiter.aws.cdk.constructs.DatabaseCreatorProps;
import de.fred4jupiter.aws.cdk.constructs.ecs.fargate.EcsFargateCreator;
import de.fred4jupiter.aws.cdk.constructs.ecs.fargate.EcsFargateCreatorProps;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EcsWithFargateAndLoadBalancerStack extends Stack {

    private static final String DB_NAME = "fredbetdb";
    private static final String DB_USERNAME = "fredbet";

    public EcsWithFargateAndLoadBalancerStack(Construct scope, String id) {
        this(scope, id, null);
    }

    public EcsWithFargateAndLoadBalancerStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);

        final Vpc vpc = createVpc(id);

        DatabaseCreatorProps databaseCreatorProps = DatabaseCreatorProps.builder()
                .databaseName(DB_NAME)
                .username(DB_USERNAME)
                .vpc(vpc)
                .subnetType(SubnetType.PRIVATE)
                .build();
        DatabaseCreator databaseCreator = new DatabaseCreator(this, "MyRdsDatabase", databaseCreatorProps);

        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("SPRING_PROFILES_ACTIVE", "mysql");
        envVariables.put("SPRING_DATASOURCE_HIKARI_JDBC_URL", databaseCreator.getJdbcUrl());
        envVariables.put("SPRING_DATASOURCE_HIKARI_USERNAME", DB_NAME);
        envVariables.put("SPRING_DATASOURCE_HIKARI_PASSWORD", databaseCreator.getSecret().getSecretArn());

        EcsFargateCreatorProps ecsFargateCreatorProps = EcsFargateCreatorProps.builder()
                .imageName("fred4jupiter/fredbet:latest").envVariables(envVariables).vpc(vpc).build();
        new EcsFargateCreator(this, "MyEcsCluster", ecsFargateCreatorProps);
    }

    private Vpc createVpc(String id) {
        SubnetConfiguration publicSubnet = SubnetConfiguration.builder().name("public-sn").subnetType(SubnetType.PUBLIC).cidrMask(24).build();
        SubnetConfiguration privateSubnet = SubnetConfiguration.builder().name("private-sn").subnetType(SubnetType.PRIVATE).cidrMask(24).build();
        return Vpc.Builder.create(this, id).maxAzs(2).subnetConfiguration(Arrays.asList(publicSubnet, privateSubnet)).natGateways(1).build();
    }
}
