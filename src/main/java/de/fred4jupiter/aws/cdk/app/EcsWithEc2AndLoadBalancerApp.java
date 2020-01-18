package de.fred4jupiter.aws.cdk.app;

import de.fred4jupiter.aws.cdk.stack.DatabaseResult;
import de.fred4jupiter.aws.cdk.stack.Ec2Stack;
import de.fred4jupiter.aws.cdk.stack.RdsStack;
import de.fred4jupiter.aws.cdk.stack.VpcStack;
import software.amazon.awscdk.core.App;

public class EcsWithEc2AndLoadBalancerApp {

    private static final String DB_NAME = "fredbetdb";
    private static final String DB_USERNAME = "fredbet";

    public EcsWithEc2AndLoadBalancerApp() {
        App app = new App();

        VpcStack vpcStack = new VpcStack(app, "VpcStack");

        DatabaseResult databaseResult = new RdsStack(app, "RdsStack").createDatabaseInstance(vpcStack, DB_NAME, DB_USERNAME);

        new Ec2Stack(app, "Ec2Stack")
                .createResources(vpcStack.getVpc(), DB_NAME, databaseResult.getDatabaseInstance(), DB_USERNAME, databaseResult.getSecret());
        app.synth();
    }
}
