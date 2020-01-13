package de.fred4jupiter.aws.cdk;

import de.fred4jupiter.aws.cdk.stack.Ec2Stack;
import de.fred4jupiter.aws.cdk.stack.FargateStack;
import software.amazon.awscdk.core.App;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AwsCdkExampleAppTest {

    private final static ObjectMapper JSON = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void testStack() {
        App app = new App();
//        Ec2Stack stack = new Ec2Stack(app, "ec2Stack");
        FargateStack stack = new FargateStack(app, "fargateStack");

        // synthesize the stack to a CloudFormation template and compare against
        // a checked-in JSON file.
        JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());
        System.out.println(actual);
//        assertEquals(new ObjectMapper().createObjectNode(), actual);
    }
}
