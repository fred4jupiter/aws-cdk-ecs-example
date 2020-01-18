package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.AddCapacityOptions;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.RepositoryImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedEc2Service;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.secretsmanager.Secret;

import java.util.HashMap;
import java.util.Map;

public class Ec2Stack extends Stack {

    public Ec2Stack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public Ec2Stack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
    }

    public void createResources(Vpc vpc, String dbName, DatabaseInstance databaseInstance, String dbUsername, Secret secret) {
        Cluster cluster = createCluster(vpc);

        // Create a load-balanced Fargate service and make it public
        RepositoryImage repositoryImage = ContainerImage.fromRegistry("fred4jupiter/fredbet:latest");

        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("SPRING_PROFILES_ACTIVE", "mysql");
        envVariables.put("SPRING_DATASOURCE_HIKARI_JDBC_URL", "jdbc:mysqldb://" + databaseInstance.getDbInstanceEndpointAddress() + ":" + databaseInstance.getDbInstanceEndpointPort() + "/" + dbName);
        envVariables.put("SPRING_DATASOURCE_HIKARI_USERNAME", dbUsername);
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
