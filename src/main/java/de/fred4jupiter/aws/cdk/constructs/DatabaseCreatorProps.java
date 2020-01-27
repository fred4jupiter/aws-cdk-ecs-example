package de.fred4jupiter.aws.cdk.constructs;

import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;

public interface DatabaseCreatorProps {

    public static Builder builder() {
        return new Builder();
    }

    String getUsername();

    Vpc getVpc();

    String getDatabaseName();

    SubnetType getSubnetType();

    // The builder for the props interface
    public static class Builder {
        private Vpc vpc;
        private String username;
        private String databaseName;
        private SubnetType subnetType;

        public Builder vpc(Vpc vpc) {
            this.vpc = vpc;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder subnetType(SubnetType subnetType) {
            this.subnetType = subnetType;
            return this;
        }


        public DatabaseCreatorProps build() {
            if (this.vpc == null) {
                throw new NullPointerException("The vpc property is required!");
            }

            if (this.username == null) {
                throw new NullPointerException("The username property is required!");
            }

            if (this.databaseName == null) {
                throw new NullPointerException("The databaseName property is required!");
            }

            if (this.subnetType == null) {
                throw new NullPointerException("The subnetType property is required!");
            }

            return new DatabaseCreatorProps() {

                @Override
                public String getUsername() {
                    return username;
                }

                @Override
                public Vpc getVpc() {
                    return vpc;
                }

                @Override
                public String getDatabaseName() {
                    return databaseName;
                }

                @Override
                public SubnetType getSubnetType() {
                    return subnetType;
                }
            };
        }
    }
}
