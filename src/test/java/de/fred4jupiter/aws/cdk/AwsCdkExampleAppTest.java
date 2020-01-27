package de.fred4jupiter.aws.cdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.fred4jupiter.aws.cdk.stack.EcsWithEc2AndLoadBalancerStack;
import de.fred4jupiter.aws.cdk.stack.EcsWithFargateAndLoadBalancerStack;
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
    public void ecsWithFargateAndLoadBalancerStack() {
        App app = new App();
        EcsWithFargateAndLoadBalancerStack stack = new EcsWithFargateAndLoadBalancerStack(app, "ecsWithFargateAndLoadBalancerStack");
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        System.out.println(actual);
        assertNotNull(actual);
    }
}
