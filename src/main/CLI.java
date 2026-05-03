package main;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "cli",
        subcommands = { ListCommand.class, CartCommand.class }
)

public class CLI implements Runnable {

    @Option(names = "--url")
    private String url;

    public void run() { }

    public static void main(String[] args) {
        new CommandLine(new CLI()).execute(args);
    }

    public String getUrl() { return url; }
}
