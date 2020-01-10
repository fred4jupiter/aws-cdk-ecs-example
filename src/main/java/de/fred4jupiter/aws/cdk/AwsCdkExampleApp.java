package de.fred4jupiter.aws.cdk;

import de.fred4jupiter.aws.cdk.stack.Ec2Stack;
import software.amazon.awscdk.core.App;

public class AwsCdkExampleApp {

    public static void main(final String[] args) {
        App app = new App();
        new Ec2Stack(app, "Ec2Stack");
        app.synth();
    }
}
