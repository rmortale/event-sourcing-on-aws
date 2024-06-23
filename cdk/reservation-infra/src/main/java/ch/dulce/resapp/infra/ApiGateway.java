package ch.dulce.resapp.infra;

import ch.dulce.resapp.infra.cdk.AppEnv;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;

import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonList;

public class ApiGateway {

    public static void main(String[] args) {
        App app = new App();

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        Objects.requireNonNull(environmentName, "context variable 'environmentName' must not be null");

        String applicationName = (String) app.getNode().tryGetContext("applicationName");
        Objects.requireNonNull(applicationName, "context variable 'applicationName' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        Objects.requireNonNull(region, "please set region in context!");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        Objects.requireNonNull(accountId, "please set accountId in context!");

        Environment awsEnvironment = makeEnv(accountId, region);

        AppEnv appEnv = new AppEnv(applicationName, environmentName);

        Stack gatewayStack = new Stack(app, "GatewayStack", StackProps.builder()
                .stackName(appEnv.prefix("Gateway"))
                .env(awsEnvironment)
                .build());

        RestApi api = RestApi.Builder.create(gatewayStack, "Gateway")
                .restApiName("RestApi Service")
                .description("Rest Api Service for Canary Deployment Demo")
                .endpointTypes(singletonList(EndpointType.REGIONAL))
                .retainDeployments(Boolean.FALSE)
                .deployOptions(StageOptions.builder().stageName("prod").variables(Map.of("lambdaAlias", "Prod")).build())
                .build();

        app.synth();
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}
