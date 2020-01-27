package de.fred4jupiter.aws.cdk.constructs.ecs.fargate;

import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Map;

public interface EcsFargateCreatorProps {

    public static EcsFargateCreatorProps.Builder builder() {
        return new EcsFargateCreatorProps.Builder();
    }

    String getImageName();

    Map<String, String> getEnvVariables();

    Vpc getVpc();

    // The builder for the props interface
    public static class Builder {
        private Vpc vpc;
        private String imageName;
        private Map<String, String> envVariables;

        public Builder imageName(String imageName) {
            this.imageName = imageName;
            return this;
        }

        public Builder envVariables(Map<String, String> envVariables) {
            this.envVariables = envVariables;
            return this;
        }

        public Builder vpc(Vpc vpc) {
            this.vpc = vpc;
            return this;
        }

        public EcsFargateCreatorProps build() {
            if (this.imageName == null) {
                throw new NullPointerException("The imageName property is required!");
            }

            if (this.envVariables == null) {
                throw new NullPointerException("The envVariables property is required!");
            }

            if (this.vpc == null) {
                throw new NullPointerException("The vpc property is required!");
            }

            return new EcsFargateCreatorProps() {

                @Override
                public String getImageName() {
                    return imageName;
                }

                @Override
                public Map<String, String> getEnvVariables() {
                    return envVariables;
                }

                @Override
                public Vpc getVpc() {
                    return vpc;
                }
            };
        }
    }
}
