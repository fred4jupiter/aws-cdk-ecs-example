package de.fred4jupiter.aws.cdk.constructs.ecs.ec2;

import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Map;

public interface EcsEc2CreatorProps {

    public static EcsEc2CreatorProps.Builder builder() {
        return new EcsEc2CreatorProps.Builder();
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

        public EcsEc2CreatorProps build() {
            if (this.imageName == null) {
                throw new NullPointerException("The imageName property is required!");
            }

            if (this.envVariables == null) {
                throw new NullPointerException("The envVariables property is required!");
            }

            if (this.vpc == null) {
                throw new NullPointerException("The vpc property is required!");
            }

            return new EcsEc2CreatorProps() {

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
