package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;

public class FargateStack extends Stack {

    public FargateStack(Construct scope, String id) {
        super(scope, id);
    }

    public FargateStack(Construct scope, String id, StackProps props) {
        super(scope, id, props);

        createStackWithFargate();
    }

    private void createStackWithFargate() {
        // Default is all AZs in region
        Vpc vpc = Vpc.Builder.create(this, "FargateVpc").maxAzs(2).build();

        Cluster cluster = Cluster.Builder.create(this, "FargateCluster").vpc(vpc).build();

        // Create a load-balanced Fargate service and make it public
        ApplicationLoadBalancedTaskImageOptions taskImageOptions = ApplicationLoadBalancedTaskImageOptions.builder()
                .image(ContainerImage.fromRegistry("fred4jupiter/fredbet:latest"))
                .containerPort(8080)
                .build();

        ApplicationLoadBalancedFargateService.Builder.create(this, "FargateService")
                .cluster(cluster)           // Required
                .desiredCount(1)            // Default is 1
                .taskImageOptions(taskImageOptions)
                .memoryLimitMiB(1024)       // Default is 512
                .publicLoadBalancer(true)   // Default is false
                .build();
    }
}
