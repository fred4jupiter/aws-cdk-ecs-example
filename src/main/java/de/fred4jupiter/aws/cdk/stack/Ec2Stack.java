package de.fred4jupiter.aws.cdk.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.AddCapacityOptions;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.RepositoryImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedEc2Service;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;

import java.util.Arrays;

public class Ec2Stack extends Stack {

    public Ec2Stack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public Ec2Stack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Default is all AZs in region
        SubnetConfiguration publicSubnet = SubnetConfiguration.builder().name("public-sn").subnetType(SubnetType.PUBLIC).cidrMask(24).build();
//        SubnetConfiguration privateSubnet = SubnetConfiguration.builder().name("private-sn").subnetType(SubnetType.PRIVATE).cidrMask(24).build();

        Vpc vpc = Vpc.Builder.create(this, "MyVpc").maxAzs(2).subnetConfiguration(Arrays.asList(publicSubnet)).natGateways(0).build();

        Cluster cluster = Cluster.Builder.create(this, "MyCluster").vpc(vpc).build();
        AddCapacityOptions addCapacityOptions = AddCapacityOptions.builder().instanceType(new InstanceType("t3.small")).build();
        cluster.addCapacity("cluster-capacity", addCapacityOptions);

        // Create a load-balanced Fargate service and make it public
        RepositoryImage repositoryImage = ContainerImage.fromRegistry("fred4jupiter/fredbet:latest");
        ApplicationLoadBalancedTaskImageOptions taskImageOptions = ApplicationLoadBalancedTaskImageOptions.builder()
                .image(repositoryImage)
                .containerPort(8080)
                .build();

        ApplicationLoadBalancedEc2Service.Builder.create(this, "MyEcsService")
                .cluster(cluster)           // Required
                .desiredCount(1)            // Default is 1
                .taskImageOptions(taskImageOptions)
                .memoryLimitMiB(1024)       // Default is 512
                .publicLoadBalancer(true)   // Default is false
                .build();
    }
}
