package com.ocado.panda.newrelic.deployment.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.ocado.panda.newrelic.api.model.deployments.Deployment;
import com.ocado.panda.newrelic.deployment.NewRelicDeployment;

import java.util.List;

@Parameters(separators = "=")
public class NewRelicDeploymentCli {

    private enum Action {
        list, mark, remove
    }

    @Parameter(names = {"--api-key"}, description = "NewRelic admin API key", required = true)
    private String newRelicApiKey;

    @Parameter(names = {"--application"}, description = "Application name in NewRelic", required = true)
    private String applicationName;

    @Parameter(names = {"--action"}, description = "Action to perform", required = true)
    private Action action;

    @Parameter(names = "--revision", description = "Deployment revision - required for 'mark' option")
    private String revision;

    @Parameter(names = "--changelog", description = "Deployment changelog - optional for 'mark' option")
    private String changelog;

    @Parameter(names = "--description", description = "Deployment description - optional for 'mark' option")
    private String description;

    @Parameter(names = "--user", description = "Deployment user - optional for 'mark' option")
    private String user;

    @Parameter(names = "--deploymentId", description = "Deployment Id to remove - required for 'remove' option")
    private Integer deploymentId;

    @Parameter(names = "--debug", description = "Debug mode")
    private boolean debug = false;

    @Parameter(names = "--help", description = "Display usage description", help = true)
    private boolean help;

    private Deployment getDeployment() {
        return Deployment.builder()
                .revision(revision)
                .changelog(changelog)
                .description(description)
                .user(user)
                .build();
    }

    public static void main(String[] args) {
        NewRelicDeploymentCli cli = new NewRelicDeploymentCli();
        if (parseMainArgument(args, cli)) {
            if (cli.debug) {
                System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
            } else {
                System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
            }
            switch (cli.action) {
                case list:
                    listDeployments(cli);
                    break;
                case mark:
                    markDeployment(cli);
                    break;
                case remove:
                    removeDeployment(cli);
                    break;
            }
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        JCommander jCommander = new JCommander(new NewRelicDeploymentCli());
        jCommander.setProgramName(NewRelicDeploymentCli.class.getSimpleName());
        jCommander.usage();
    }

    private static boolean parseMainArgument(String[] args, NewRelicDeploymentCli cli) {
        try {
            new JCommander(cli, args);
            if (cli.help) {
                return false;
            }
            switch (cli.action) {
                case list:
                    break;
                case mark:
                    assertParameterRequired(cli.action, cli.revision, "--revision");
                    break;
                case remove:
                    assertParameterRequired(cli.action, cli.deploymentId, "--deploymentId");
                    break;
            }
            return true;
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private static void assertParameterRequired(Action cliAction, Object parameterValue, String parameterName) {
        if (parameterValue == null) {
            throw new ParameterException(String.format("Parameter %s is required for action '%s'", parameterName, cliAction));
        }
    }

    private static void listDeployments(NewRelicDeploymentCli cli) {
        List<Deployment> deployments = new NewRelicDeployment(cli.newRelicApiKey).list(
                cli.applicationName
        );
        System.out.println("Deployments: " + deployments);
    }

    private static void markDeployment(NewRelicDeploymentCli cli) {
        Deployment deployment = new NewRelicDeployment(cli.newRelicApiKey).mark(
                cli.applicationName,
                cli.getDeployment()
        );
        System.out.println("Deployment marked: " + deployment);
    }

    private static void removeDeployment(NewRelicDeploymentCli cli) {
        Deployment deployment = new NewRelicDeployment(cli.newRelicApiKey).remove(
                cli.applicationName,
                cli.deploymentId
        );
        System.out.println("Deployment removed: " + deployment);
    }

}
