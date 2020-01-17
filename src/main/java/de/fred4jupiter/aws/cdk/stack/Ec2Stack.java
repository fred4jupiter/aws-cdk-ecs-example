package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedEc2Service;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.rds.DatabaseInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ec2Stack extends Stack {

    private static final String DB_NAME = "fredbetdb";
    private static final String DB_USERNAME = "fredbet";
    private static final String DB_PASSWORD = "fredbet";

    public Ec2Stack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public Ec2Stack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Default is all AZs in region
        SubnetConfiguration publicSubnet = SubnetConfiguration.builder().name("public-sn").subnetType(SubnetType.PUBLIC).cidrMask(24).build();
        SubnetConfiguration privateSubnet = SubnetConfiguration.builder().name("private-sn").subnetType(SubnetType.PRIVATE).cidrMask(24).build();

        Vpc vpc = Vpc.Builder.create(this, "MyVpc").maxAzs(2).subnetConfiguration(Arrays.asList(publicSubnet, privateSubnet)).natGateways(0).build();

        Cluster cluster = Cluster.Builder.create(this, "MyCluster").vpc(vpc).build();

        AddCapacityOptions addCapacityOptions = AddCapacityOptions.builder().instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.SMALL)).build();
        cluster.addCapacity("cluster-capacity", addCapacityOptions);

        // Create a load-balanced Fargate service and make it public
        RepositoryImage repositoryImage = ContainerImage.fromRegistry("fred4jupiter/fredbet:latest");

        RdsStack rdsStack = new RdsStack(scope, id);
        DatabaseInstance databaseInstance = rdsStack.createDatabaseInstance(DB_NAME, DB_USERNAME, DB_PASSWORD);

        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("SPRING_PROFILES_ACTIVE", "dev");
        envVariables.put("SPRING_DATASOURCE_HIKARI_JDBC_URL", "jdbc:mysqldb://" + databaseInstance.getDbInstanceEndpointAddress() + ":" + databaseInstance.getDbInstanceEndpointPort() + "/" + DB_NAME);
        envVariables.put("SPRING_DATASOURCE_HIKARI_USERNAME", DB_USERNAME);
        envVariables.put("SPRING_DATASOURCE_HIKARI_PASSWORD", DB_PASSWORD);

        ApplicationLoadBalancedTaskImageOptions taskImageOptions = ApplicationLoadBalancedTaskImageOptions.builder()
                .image(repositoryImage)
                .containerPort(8080)
                .environment(envVariables)
                .build();

        ApplicationLoadBalancedEc2Service.Builder.create(this, "MyEcsService")
                .cluster(cluster)           // Required
                .desiredCount(1)            // Default is 1
                .taskImageOptions(taskImageOptions)
                .memoryLimitMiB(1024)       // Default is 512
                .publicLoadBalancer(true)   // Default is false
                .healthCheckGracePeriod(Duration.seconds(60))
                .serviceName("fredbet-service")
                .build();
    }
}
