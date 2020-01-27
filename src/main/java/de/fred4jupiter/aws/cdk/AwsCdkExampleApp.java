package de.fred4jupiter.aws.cdk;

import de.fred4jupiter.aws.cdk.stack.EcsWithEc2AndLoadBalancerStack;
import software.amazon.awscdk.core.App;

public class AwsCdkExampleApp {

    public static void main(final String[] args) {
        App app = new App();
        new EcsWithEc2AndLoadBalancerStack(app, "ecsWithEc2AndLoadBalancerStack");
//        new DatabaseOnlyStack(app, "databaseOnlyStack");
//        new SecretStack(app, "secretStack");
        app.synth();
    }
}
