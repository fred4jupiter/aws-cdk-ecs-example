package de.fred4jupiter.aws.cdk;

import de.fred4jupiter.aws.cdk.stack.Ec2Stack;
import de.fred4jupiter.aws.cdk.stack.FargateStack;
import software.amazon.awscdk.core.App;

public class AwsCdkExampleApp {

    public static void main(final String[] args) {
//        createEcsWithFargateApplication();

        createEcsWithEc2Application();
    }

    private static void createEcsWithEc2Application() {
        App app = new App();
        new Ec2Stack(app, "Ec2Stack");
        app.synth();
    }

    private static void createEcsWithFargateApplication() {
        App app = new App();
        new FargateStack(app, "FargateStack");
        app.synth();
    }
}
