package de.fred4jupiter.aws.cdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.fred4jupiter.aws.cdk.stack.EcsWithEc2AndLoadBalancerStack;
import de.fred4jupiter.aws.cdk.stack.FargateStack;
import de.fred4jupiter.aws.cdk.stack.RdsStack;
import de.fred4jupiter.aws.cdk.stack.VpcStack;
import org.junit.Test;
import software.amazon.awscdk.core.App;

import static org.junit.Assert.assertNotNull;

public class AwsCdkExampleAppTest {

    private final static ObjectMapper JSON = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void ecsWithEc2AndLoadBalancerStack() {
        App app = new App();
        EcsWithEc2AndLoadBalancerStack stack = new EcsWithEc2AndLoadBalancerStack(app, "ecsWithEc2AndLoadBalancerStack");
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        System.out.println(actual);
        assertNotNull(actual);
    }

    @Test
    public void createVpcStack() {
        App app = new App();
        VpcStack stack = new VpcStack(app, "vpcStack");
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        System.out.println(actual);
        assertNotNull(actual);
    }

    @Test
    public void createRdsStack() {
        App app = new App();
        RdsStack stack = new RdsStack(app, "rdsStack");
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        System.out.println(actual);
        assertNotNull(actual);
    }

    @Test
    public void createFargateStack() {
        App app = new App();
        FargateStack stack = new FargateStack(app, "fargateStack");
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        System.out.println(actual);
        assertNotNull(actual);
    }

}
