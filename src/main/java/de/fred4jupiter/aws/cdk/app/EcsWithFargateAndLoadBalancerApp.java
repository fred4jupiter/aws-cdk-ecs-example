package de.fred4jupiter.aws.cdk.app;

import de.fred4jupiter.aws.cdk.stack.FargateStack;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.cxapi.CloudAssembly;

public class EcsWithFargateAndLoadBalancerApp {

    public CloudAssembly create() {
        App app = new App();
        new FargateStack(app, "FargateStack");
        return app.synth();
    }
}
