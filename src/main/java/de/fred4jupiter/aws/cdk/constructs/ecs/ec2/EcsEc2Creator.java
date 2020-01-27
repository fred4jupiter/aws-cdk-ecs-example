package de.fred4jupiter.aws.cdk.constructs.ecs.ec2;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
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

public class EcsEc2Creator extends Construct {

    public EcsEc2Creator(Construct scope, String id, EcsEc2CreatorProps props) {
        super(scope, id);

        // Create a load-balanced Fargate service and make it public
        RepositoryImage repositoryImage = ContainerImage.fromRegistry(props.getImageName());

        ApplicationLoadBalancedTaskImageOptions taskImageOptions = ApplicationLoadBalancedTaskImageOptions.builder()
                .image(repositoryImage)
                .containerPort(8080)
                .environment(props.getEnvVariables())
                .build();

        Cluster cluster = createCluster(props.getVpc());

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
