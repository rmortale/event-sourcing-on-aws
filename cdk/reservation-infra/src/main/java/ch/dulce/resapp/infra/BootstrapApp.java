package ch.dulce.resapp.infra;

import software.amazon.awscdk.App;

import java.util.Objects;

public class BootstrapApp {

    public static void main(String[] args) {
        App app = new App();

        String region = (String) app.getNode().tryGetContext("region");
        Objects.requireNonNull(region, "please set region in context!");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        Objects.requireNonNull(accountId, "please set accountId in context!");




        app.synth();
    }
}
