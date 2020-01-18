package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ecs.AddCapacityOptions;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.RepositoryImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedEc2Service;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.secretsmanager.Secret;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EcsWithEc2AndLoadBalancerStack extends Stack {

    private static final String DB_NAME = "fredbetdb";
    private static final String DB_USERNAME = "fredbet";

    public EcsWithEc2AndLoadBalancerStack(Construct scope, String id) {
        this(scope, id, null);
    }

    public EcsWithEc2AndLoadBalancerStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);

        SubnetConfiguration publicSubnet = SubnetConfiguration.builder().name("public-sn").subnetType(SubnetType.PUBLIC).cidrMask(24).build();
        SubnetConfiguration privateSubnet = SubnetConfiguration.builder().name("private-sn").subnetType(SubnetType.PRIVATE).cidrMask(24).build();
        SubnetConfiguration isolatedSubnet = SubnetConfiguration.builder().name("isolated-sn").subnetType(SubnetType.ISOLATED).cidrMask(24).build();

        final Vpc vpc = Vpc.Builder.create(this, "MyVpc").maxAzs(1).subnetConfiguration(Arrays.asList(publicSubnet, isolatedSubnet)).natGateways(0).build();

        Cluster cluster = createCluster(vpc);

        // Create a load-balanced Fargate service and make it public
        RepositoryImage repositoryImage = ContainerImage.fromRegistry("fred4jupiter/fredbet:latest");

        Secret secret = Secret.Builder.create(this, "database-password").build();

        DatabaseInstance databaseInstance = DatabaseInstance.Builder.create(this, "fredbet-db")
                .vpc(vpc)
                .backupRetention(Duration.days(3))
                .allocatedStorage(10)
                .characterSetName("UTF-8")
                .engine(DatabaseInstanceEngine.MYSQL)
                .databaseName(DB_NAME)
                .masterUsername(DB_USERNAME)
                .masterUserPassword(secret.getSecretValue())
                .multiAz(false)
                .instanceClass(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
                .vpcPlacement(SubnetSelection.builder().subnetType(SubnetType.ISOLATED).build())
                .availabilityZone("eu-central-1a")
                .build();

        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("SPRING_PROFILES_ACTIVE", "mysql");
        envVariables.put("SPRING_DATASOURCE_HIKARI_JDBC_URL", "jdbc:mysqldb://" + databaseInstance.getDbInstanceEndpointAddress() + ":" + databaseInstance.getDbInstanceEndpointPort() + "/" + DB_NAME);
        envVariables.put("SPRING_DATASOURCE_HIKARI_USERNAME", DB_USERNAME);
        envVariables.put("SPRING_DATASOURCE_HIKARI_PASSWORD", secret.getSecretArn());

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

    private Cluster createCluster(Vpc vpc) {
        Cluster cluster = Cluster.Builder.create(this, "MyCluster").vpc(vpc).build();

        AddCapacityOptions addCapacityOptions = AddCapacityOptions.builder().instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.SMALL)).build();
        cluster.addCapacity("cluster-capacity", addCapacityOptions);
        return cluster;
    }

}
