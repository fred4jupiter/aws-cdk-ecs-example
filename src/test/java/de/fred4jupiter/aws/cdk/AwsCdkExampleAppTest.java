package de.fred4jupiter.aws.cdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.fred4jupiter.aws.cdk.stack.DatabaseOnlyStack;
import de.fred4jupiter.aws.cdk.stack.EcsWithEc2AndLoadBalancerStack;
import de.fred4jupiter.aws.cdk.stack.EcsWithFargateAndLoadBalancerStack;
import de.fred4jupiter.aws.cdk.stack.SecretStack;
import org.junit.Test;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Stack;

import java.util.function.Function;

import static org.junit.Assert.assertNotNull;

public class AwsCdkExampleAppTest {

    private final static ObjectMapper JSON = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void ecsWithEc2AndLoadBalancerStack() {
        createAndCheckStack(app -> new EcsWithEc2AndLoadBalancerStack(app, "ecsWithEc2AndLoadBalancerStack"));
    }

    @Test
    public void ecsWithFargateAndLoadBalancerStack() {
        createAndCheckStack(app -> new EcsWithFargateAndLoadBalancerStack(app, "ecsWithFargateAndLoadBalancerStack"));
    }

    @Test
    public void databaseOnlyStack() {
        createAndCheckStack(app ->  new DatabaseOnlyStack(app, "databaseOnlyStack"));
    }

    @Test
    public void secretStack() {
        createAndCheckStack(app ->  new SecretStack(app, "secretStack"));
    }

    private void createAndCheckStack(Function<App, Stack> function) {
        App app = new App();
        Stack stack = function.apply(app);
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        System.out.println(actual);
        assertNotNull(actual);
    }
}
